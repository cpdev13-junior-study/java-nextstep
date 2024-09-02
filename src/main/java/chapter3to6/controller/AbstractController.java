package chapter3to6.controller;

import chapter3to6.model.Method;
import chapter3to6.util.HttpRequest;
import chapter3to6.util.HttpResponse;

public abstract class AbstractController implements Controller {
    @Override
    public void service(HttpRequest request, HttpResponse response) {
        if (request.getMethod() == Method.GET) {
            doGet(request, response);
        } else {
            doPost(request, response);
        }
    }

    protected void doGet(HttpRequest request, HttpResponse response) {
    }

    protected void doPost(HttpRequest request, HttpResponse response) {
    }
}
