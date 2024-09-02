package chapter6to12.next.web.Controller;


import chapter6to12.core.db.DataBase;
import chapter6to12.next.model.User;
import chapter6to12.next.mvc.AbstractController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ListUserController extends AbstractController {

    @Override
    protected String doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/index.jsp";
        } else {
            req.setAttribute("users", DataBase.findAll());
            return "/user/list.jsp";
        }

    }
}
