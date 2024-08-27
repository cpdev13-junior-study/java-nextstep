package webserver;

import db.DataBase;
import model.User;
import util.Controller;
import util.HttpRequest;
import util.HttpResponse;

import java.io.IOException;

public class UserCreateController implements Controller {

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        User user = new User(request.getParameter("userId"), request.getParameter("password"), request.getParameter("name"), request.getParameter("email"));
        DataBase.addUser(user);

        response.sendRedirect("/index.html");
    }
}
