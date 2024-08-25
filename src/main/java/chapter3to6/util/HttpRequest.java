package chapter3to6.util;

import chapter3to6.model.Method;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public class HttpRequest {

    private Map<String, String> headerMap;
    private Map<String, String> requestLineMap;
    private Map<String, String> paramMap;
    private Map<String, String> cookies;

    public HttpRequest(InputStream inputStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        headerMap = new HashMap<>();
        paramMap = new HashMap<>();
        cookies = new HashMap<>();
        requestLineMap = new HashMap<>();
        parseHttpRequest(in);
    }

    private void parseHttpRequest(BufferedReader in) throws IOException {
        String line = in.readLine();
        if (line.isEmpty()) {
            return;
        }
        String requestLine = line;
        while ((line = in.readLine()) != null && !line.equals("")) {
            if (HttpRequestUtils.parseHeader(line) != null) {
                HttpRequestUtils.Pair headerPair = HttpRequestUtils.parseHeader(line);
                headerMap.put(headerPair.getKey(), headerPair.getValue());
            }
        }

        parseRequestLine(requestLine);
        parseRequestBody(in);
        cookies = HttpRequestUtils.parseCookies(headerMap.get("Cookie"));
    }

    private void parseRequestLine(String requestLine) {
        requestLineMap.put("Method", requestLine.split(" ")[0]);

        String pathWithQueryString = requestLine.split(" ")[1];
        int queryStringIndex = pathWithQueryString.indexOf("?");
        String path = pathWithQueryString;
        if (queryStringIndex != -1) {
            path = pathWithQueryString.substring(0, queryStringIndex);
            paramMap.putAll(HttpRequestUtils.parseQueryString(pathWithQueryString.substring(queryStringIndex + 1)));
        }
        requestLineMap.put("Path", path);
    }

    private void parseRequestBody(BufferedReader in) throws IOException {
        String contentLength = headerMap.get("Content-Length");
        if (contentLength != null) {
            int length = Integer.parseInt(contentLength);
            if (length > 0) {
                paramMap = HttpRequestUtils.parseQueryString(IOUtils.readData(in, length));
            }
        }
    }

    public Method getMethod() {
        return Method.find(requestLineMap.get("Method"));
    }

    public String getPath() {
        return requestLineMap.get("Path");
    }

    public String getHeader(String key) {
        return headerMap.get(key);
    }

    public String getParameter(String key) {
        return paramMap.get(key);
    }

    public String getCookie(String key) {
        return cookies.get(key);
    }

    public void addCookie(String key, String value) {
        cookies.put(key, value);
    }
}
