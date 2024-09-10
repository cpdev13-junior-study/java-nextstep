package chapter3to6.controller;

import chapter3to6.util.HttpRequest;
import chapter3to6.util.HttpResponse;

public interface Controller {
    void service(HttpRequest request, HttpResponse response);
}
