package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequest;
import util.HttpResponse;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Collection;

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
            HttpResponse response = new HttpResponse(out);

            String method = request.getMethod();
            String path = request.getPath();
            log.info("request path: {}", path);

            if ("/".equals(path)) {
                path = "/index.html";
            }

            if ("POST".equals(method) && path.startsWith("/user/create")) {
                userCreate(request, response);
                log.info("가입된 유저 목록: {}", DataBase.findAll());
                return;
            }

            if ("POST".equals(method) && path.startsWith("/user/login")) {
                userLogin(request, response);
                return;
            }

            if (path.startsWith("/user/list")) {
                userList(request, response);
                return;
            }

            staticResource(path, response);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void userCreate(HttpRequest request, HttpResponse response) throws IOException {
        User user = new User(request.getParameter("userId"), request.getParameter("password"), request.getParameter("name"), request.getParameter("email"));
        DataBase.addUser(user);

        response.sendRedirect("/index.html");
    }

    private void userLogin(HttpRequest request, HttpResponse response) throws IOException {
        User user = DataBase.findUserById(request.getParameter("userId"));

        if (user != null && user.getPassword().equals(request.getParameter("password"))) {
            response.addHeader("Set-Cookie", "logined=true; path=/");
            response.sendRedirect("/index.html");
        } else {
            response.addHeader("Set-Cookie", "logined=false; path=/");
            response.sendRedirect("/user/login_failed.html");
        }
    }

    private void userList(HttpRequest request, HttpResponse response) throws IOException {
        String cookies = request.getHeader("Cookie");
        if (cookies == null || !cookies.contains("logined=true")) {
            response.sendRedirect("/user/login.html");
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

        response.forward(sb.toString());
    }

    private void staticResource(String uri, HttpResponse response) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp" + uri).toPath());

        if (uri.endsWith(".css")) {
            response.addHeader("Content-Type", "text/css,*/*;q=0.1");
        } else {
            response.addHeader("Content-Type", "text/html;charset=utf-8");
        }

        response.forward(body);
    }
}