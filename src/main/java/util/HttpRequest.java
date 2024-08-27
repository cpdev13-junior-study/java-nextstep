package util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private final String method;
    private final String path;
    private final Map<String, String> headers;
    private final Map<String, String> parameters;

    public HttpRequest(InputStream in) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String line = br.readLine();

        String[] startLine = line.split(" ");

        this.method = startLine[0];
        String fullPath = startLine[1];
        String[] pathAndParams = fullPath.split("\\?", 2);
        this.path = pathAndParams[0];

        this.parameters = pathAndParams.length == 2 ? HttpRequestUtils.parseQueryString(pathAndParams[1]) : new HashMap<>();

        this.headers = new HashMap<>();
        while ((line = br.readLine()) != null && !line.isEmpty()) {
            String[] header = line.split(": ", 2);
            if (header.length == 2) {
                headers.put(header[0], header[1]);
            }
        }

        if (method.equals("POST")) {
            final String body = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
            parameters.clear();
            parameters.putAll(HttpRequestUtils.parseQueryString(body));
        }
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getHeader(final String name) {
        return headers.get(name);
    }

    public String getParameter(final String name) {
        return parameters.get(name);
    }
}
