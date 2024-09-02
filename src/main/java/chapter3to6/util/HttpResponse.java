package chapter3to6.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
    private DataOutputStream dos;
    private Map<String, String> headerMap;
    private Map<String, String> cookieMap;
    private static final String REDIRECT_PREFIX = "http://localhost:8080";
    private static final String FORWARD_PREFIX = "./src/main/java/chapter3to6/webapp";

    public HttpResponse(OutputStream out) {
        this.dos = new DataOutputStream(out);
        headerMap = new HashMap<>();
        cookieMap = new HashMap<>();
    }


    public void forward(String path) {
        try {
            byte[] body = Files.readAllBytes(new File(FORWARD_PREFIX + path).toPath());
            if (path.contains(".css")) {
                // content type 추가
                headerMap.put("Content-Type", "text/css;charset=utf-8");
            } else {
                headerMap.put("Content-Type", "text/html;charset=utf-8");
            }
            // content_length 추가
            headerMap.put("Content-Length", body.length + "");
            // 헤더 생성
            response200Header();
            // 바디 생성
            responseBody(body);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void forwardDirectBody(byte[] body) {
        try {
            headerMap.put("Content-Type", "text/html;charset=utf-8");
            headerMap.put("Content-Length", body.length + "");
            response200Header();
            setCookies();
            responseBody(body);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(byte[] body) throws IOException {
        dos.write(body, 0, body.length);
        dos.flush();
    }

    private void response200Header() {
        try {
            dos.writeBytes(("HTTP/1.1 200 OK \r\n"));
            setHeaders();
            setCookies();
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void setHeaders() throws IOException {
        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            dos.writeBytes(entry.getKey() + ": " + entry.getValue() + "\r\n");
        }
    }


    public void sendRedirect(String redirectUrl) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + REDIRECT_PREFIX + redirectUrl + " \r\n");
            setHeaders();
            setCookies();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void addHeader(String key, String value) {
        headerMap.put(key, value);
    }

    public void addCookie(String key, String value) {
        cookieMap.put(key, value);
    }

    public void addCookie(HttpRequest request) {
        cookieMap.putAll(request.getCookies());
    }

    private void setCookies() throws IOException {
        for (Map.Entry<String, String> entry : cookieMap.entrySet()) {
            dos.writeBytes("Set-Cookie: " + entry.getKey() + "=" + entry.getValue() + "; Path=/ \r\n");
        }
    }
}
