package chapter6to12.next.web.Controller;

import chapter6to12.core.db.DataBase;
import chapter6to12.next.model.User;
import chapter6to12.next.mvc.AbstractController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CreateUserController extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(CreateUserController.class);

    @Override
    protected String doPost(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        User user = new User(req.getParameter("userId"), req.getParameter("password"), req.getParameter("name"),
                req.getParameter("email"));
        log.debug("user : {}", user);
        DataBase.addUser(user);
        return "redirect:/user/list";
    }

    @Override
    protected String doGet(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        return "/user/form.jsp";
    }
}
