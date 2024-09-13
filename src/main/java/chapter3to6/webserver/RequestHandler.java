package chapter3to6.webserver;

import java.io.*;
import java.net.Socket;
import java.util.UUID;

import chapter3to6.controller.Controller;
import chapter3to6.util.HttpRequest;
import chapter3to6.util.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private Socket connection;
    private RequestMapping requestMapping;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        this.requestMapping = new RequestMapping();
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            HttpRequest httpRequest = new HttpRequest(in);
            HttpResponse httpResponse = new HttpResponse(out);
            if(httpRequest.getCookies().get("JSESSIONID")==null){
                httpRequest.addCookie("JSESSIONID", UUID.randomUUID().toString());
            }
            httpResponse.addCookie(httpRequest);


            Controller controller = requestMapping.getController(httpRequest.getPath());
            if (controller == null) {
                httpResponse.forward(getDefaultPath(httpRequest.getPath()));
            } else {
                controller.service(httpRequest, httpResponse);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String getDefaultPath(String path) {
        return path.equals("/") ? "/index.html" : path;
    }
}
