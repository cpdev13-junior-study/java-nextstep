package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

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
            String uri = requestLine.split(" ")[1];
            if (uri.equals("/")) {
                uri = "/index.html";
            }
            if (uri.startsWith("/user/create")) {
                userCreate(uri, out);
                log.info("가입된 유저 목록: {}", DataBase.findAll());
            }
            if (uri.equals("/index.html")) {
                index(uri, out);
            }


        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void userCreate(String uri, OutputStream out) throws IOException {
        int index = uri.indexOf("?");
        String queryString = uri.substring(index + 1);
        Map<String, String> params = HttpRequestUtils.parseQueryString(queryString);

        User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
        DataBase.addUser(user);

        DataOutputStream dos = new DataOutputStream(out);
        response200Header(dos, 0);
    }

    private void index(String uri, OutputStream out) throws IOException {
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
