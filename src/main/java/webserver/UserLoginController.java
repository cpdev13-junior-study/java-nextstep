package webserver;


import db.DataBase;
import model.User;
import util.Controller;
import util.HttpRequest;
import util.HttpResponse;

import java.io.IOException;
import java.util.Map;

public class UserLoginController implements Controller {

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        User user = DataBase.findUserById(request.getParameter("userId"));

        if (user != null && user.getPassword().equals(request.getParameter("password"))) {
            response.addHeader("Set-Cookie", "logined=true; path=/");
            response.sendRedirect("/index.html");
        } else {
            response.addHeader("Set-Cookie", "logined=false; path=/");
            response.sendRedirect("/user/login_failed.html");
        }
    }
}