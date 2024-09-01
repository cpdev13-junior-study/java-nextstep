package chapter6to12.next.web.Controller.user;


import chapter6to12.core.db.DataBase;
import chapter6to12.next.dao.UserDao;
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
        UserDao userDao = new UserDao();
        if (user == null) {
            return "redirect:/";
        } else {
            req.setAttribute("users", userDao.findAll());
            return "/user/list.jsp";
        }

    }
}
