package chapter3to6.controller;

import chapter3to6.db.DataBase;
import chapter3to6.model.User;
import chapter3to6.util.HttpRequest;
import chapter3to6.util.HttpResponse;
import chapter3to6.util.HttpSession;

public class LoginController extends AbstractController {

    @Override
    protected void doPost(HttpRequest request, HttpResponse response) {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");

        User findUser = DataBase.findUserById(userId);
        if (findUser != null && findUser.getPassword().equals(password)) {
            HttpSession session = request.getSession();
            session.setAttribute("user", findUser);
            response.sendRedirect("/index.html");
        } else {
            response.sendRedirect("/user/login_failed.html");
        }
    }
}
