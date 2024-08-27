package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequest;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Map;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest request = new HttpRequest(in);

            String method = request.getMethod();
            String path = request.getPath();
            log.info("request path: {}", path);

            if ("/".equals(path)) {
                path = "/index.html";
            }

            if ("POST".equals(method) && path.startsWith("/user/create")) {
                userCreate(request, out);
                log.info("가입된 유저 목록: {}", DataBase.findAll());
                return;
            }

            if ("POST".equals(method) && path.startsWith("/user/login")) {
                userLogin(request, out);
                return;
            }

            if (path.startsWith("/user/list")) {
                userList(request, out);
                return;
            }

            staticResource(path, out);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void userCreate(HttpRequest request, OutputStream out) throws IOException {
        User user = new User(request.getParameter("userId"), request.getParameter("password"), request.getParameter("name"), request.getParameter("email"));
        DataBase.addUser(user);

        DataOutputStream dos = new DataOutputStream(out);
        redirect(dos, "/index.html");
    }

    private void userLogin(HttpRequest request, OutputStream out) throws IOException {
        User user = DataBase.findUserById(request.getParameter("userId"));

        DataOutputStream dos = new DataOutputStream(out);
        if (user != null && user.getPassword().equals(request.getParameter("password"))) {
            redirectWithCookie(dos, "/index.html", "logined=true");
        } else {
            redirectWithCookie(dos, "/user/login_failed.html", "logined=false");
        }
    }

    private void userList(HttpRequest request, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);

        String logined = request.getHeader("Cookie");
        if (logined == null || !logined.contains("logined=true")) {
            redirect(dos, "/user/login.html");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<ul>");
        Collection<User> all = DataBase.findAll();
        for (User user : all) {
            sb.append("<li>");
            sb.append(user.getUserId());
            sb.append("</li>");
        }
        sb.append("</ul>");

        byte[] body = sb.toString().getBytes();
        response200Header(dos, body.length);
        responseBody(dos, body);
    }

    private void redirectWithCookie(DataOutputStream dos, String uri, String cookie) throws IOException {
        dos.writeBytes("HTTP/1.1 302 Found \r\n");
        dos.writeBytes("Location: " + uri + " \r\n");
        dos.writeBytes("Set-Cookie: " + cookie + "; path=/ \r\n");
        dos.writeBytes("\r\n");
    }

    private void redirect(DataOutputStream dos, String uri) throws IOException {
        dos.writeBytes("HTTP/1.1 302 Found \r\n");
        dos.writeBytes("Location: " + uri + "\r\n");
        dos.writeBytes("\r\n");
    }

    private void staticResource(String uri, OutputStream out) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp" + uri).toPath());

        DataOutputStream dos = new DataOutputStream(out);
        if (uri.endsWith(".css")) {
            response200CssHeader(dos, body.length);
        } else {
            response200Header(dos, body.length);
        }
        responseBody(dos, body);
    }

    private void response200CssHeader(DataOutputStream dos, int lengthOfBodyContent) throws IOException {
        dos.writeBytes("HTTP/1.1 200 OK \r\n");
        dos.writeBytes("Content-Type: text/css,*/*,q=0.1\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        dos.writeBytes("\r\n");
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) throws IOException {
        dos.writeBytes("HTTP/1.1 200 OK \r\n");
        dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        dos.writeBytes("\r\n");
    }

    private void responseBody(DataOutputStream dos, byte[] body) throws IOException {
        dos.write(body, 0, body.length);
        dos.flush();
    }
}
