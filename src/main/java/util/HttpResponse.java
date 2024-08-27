package util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private DataOutputStream dos;
    private Map<String, String> headers = new HashMap<>();
    private boolean headersWritten = false;

    public HttpResponse(OutputStream out) {
        this.dos = new DataOutputStream(out);
    }

    public void forward(String body) throws IOException {
        forward(body.getBytes());
    }

    public void forward(byte[] bodyBytes) throws IOException {
        addHeader("Content-Length", String.valueOf(bodyBytes.length));
        writeHeaders();
        responseBody(bodyBytes);
    }

    public void sendRedirect(String uri) throws IOException {
        dos.writeBytes("HTTP/1.1 302 Found \r\n");
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            dos.writeBytes(entry.getKey() + ": " + entry.getValue() + "\r\n");
        }
        dos.writeBytes("Location: " + uri + "\r\n");
        dos.writeBytes("\r\n");
        dos.flush(); // 헤더와 리다이렉트가 모두 기록된 후 flush 호출
    }

    public void addHeader(String field, String value) {
        headers.put(field, value);
    }

    private void writeHeaders() throws IOException {
        if (!headers.containsKey("Content-Type")) {
            addHeader("Content-Type", "text/html;charset=utf-8");
        }

        if (!headersWritten) {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                dos.writeBytes(entry.getKey() + ": " + entry.getValue() + "\r\n");
            }
            dos.writeBytes("\r\n");
            headersWritten = true;
        }
    }

    private void responseBody(byte[] body) throws IOException {
        dos.write(body, 0, body.length);
        dos.flush(); // 모든 데이터를 전송한 후에 flush 호출
    }
}