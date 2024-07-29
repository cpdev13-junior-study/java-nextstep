package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new java.io.InputStreamReader(in, StandardCharsets.UTF_8));
            String requestLine = br.readLine();
            log.info("request line: {}", requestLine);

            String[] tokens = requestLine.split(" ");
            String method = tokens[0];
            String uri = tokens[1];
            if ("/".equals(uri)) {
                uri = "/index.html";
            }
            if ("POST".equals(method) && uri.startsWith("/user/create")) {
                userCreate(br, out);
                log.info("가입된 유저 목록: {}", DataBase.findAll());
                return;
            }
            if ("POST".equals(method) && uri.startsWith("/user/login")) {
                userLogin(br, out);
                return;
            }

            staticResource(uri, out);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void userCreate(BufferedReader br, OutputStream out) throws IOException {
        int contentLength = getContentLength(br);
        String requestBody = IOUtils.readData(br, contentLength);

        Map<String, String> params = HttpRequestUtils.parseQueryString(requestBody);
        User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
        DataBase.addUser(user);

        DataOutputStream dos = new DataOutputStream(out);
        redirect(dos, "/index.html");
    }

    private void userLogin(BufferedReader br, OutputStream out) throws IOException {
        int contentLength = getContentLength(br);
        String requestBody = IOUtils.readData(br, contentLength);

        Map<String, String> params = HttpRequestUtils.parseQueryString(requestBody);
        User user = DataBase.findUserById(params.get("userId"));

        DataOutputStream dos = new DataOutputStream(out);
        if (user != null && user.getPassword().equals(params.get("password"))) {
            redirectWithCookie(dos, "/index.html", "logined=true");
        }
        redirectWithCookie(dos, "/user/login_failed.html", "logined=false");
    }

    private void redirectWithCookie(DataOutputStream dos, String uri, String cookie) throws IOException{
        dos.writeBytes("HTTP/1.1 302 Found \r\n");
        dos.writeBytes("Location: " + uri + " \r\n");
        dos.writeBytes("Set-Cookie: " + cookie + "; path=/ \r\n");
        dos.writeBytes("\r\n");
    }

    private int getContentLength(BufferedReader br) throws IOException {
        int contentLength = 0;
        String line;
        while ((line = br.readLine()) != null && !line.isEmpty()) {
            log.debug("header: {}", line);
            if (line.contains("Content-Length")) {
                contentLength = Integer.parseInt(line.split(":")[1].trim());
            }
        }
        return contentLength;
    }

    private void redirect(DataOutputStream dos, String uri) throws IOException {
        dos.writeBytes("HTTP/1.1 302 Found \r\n");
        dos.writeBytes("Location: " + uri + "\r\n");
        dos.writeBytes("\r\n");
    }

    private void staticResource(String uri, OutputStream out) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp" + uri).toPath());

        DataOutputStream dos = new DataOutputStream(out);
        response200Header(dos, body.length);
        responseBody(dos, body);
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
