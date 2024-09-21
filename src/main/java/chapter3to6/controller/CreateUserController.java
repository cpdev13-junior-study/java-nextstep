package chapter3to6.controller;


import chapter3to6.db.DataBase;
import chapter3to6.model.User;
import chapter3to6.util.HttpRequest;
import chapter3to6.util.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateUserController extends AbstractController{

    private static final Logger log = LoggerFactory.getLogger(CreateUserController.class);

    @Override
    protected void doPost(HttpRequest request, HttpResponse response) {
        User user = new User(
                request.getParameter("userId"),
                request.getParameter("password"),
                request.getParameter("name"),
                request.getParameter("email")
        );
        log.info("회원가입 결과 = {}", user);
        DataBase.addUser(user);
        response.sendRedirect("/index.html");
    }
}
