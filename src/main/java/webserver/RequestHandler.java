package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Controller;
import util.HttpRequest;
import util.HttpResponse;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private Socket connection;
    private Map<String, Controller> controllerMap = new HashMap<>();

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        initializeControllers();
    }

    private void initializeControllers() {
        controllerMap.put("/user/create", new UserCreateController());
        controllerMap.put("/user/login", new UserLoginController());
        controllerMap.put("/user/list", new UserListController());
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest request = new HttpRequest(in);
            HttpResponse response = new HttpResponse(out);

            String path = request.getPath();
            log.info("request path: {}", path);

            if ("/".equals(path)) {
                path = "/index.html";
            }

            Controller controller = controllerMap.get(path);
            if (controller != null) {
                controller.service(request, response);
            } else {
                staticResource(path, response);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
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