package webserver;

import db.DataBase;
import model.User;
import util.Controller;
import util.HttpRequest;
import util.HttpResponse;

import java.io.IOException;
import java.util.Collection;

public class UserListController implements Controller {

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        String cookies = request.getHeader("Cookie");
        if (cookies == null || !cookies.contains("logined=true")) {
            response.sendRedirect("/user/login.html");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<ul>");
        Collection<User> all = DataBase.findAll();
        for (User user : all) {
            sb.append("<li>");
            sb.append(user.getUserId());
            sb.append("</li>");
        }
        sb.append("</ul>");

        response.forward(sb.toString());
    }
}