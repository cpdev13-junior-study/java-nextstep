package chapter6to12.next.web.controller.user;


import chapter6to12.next.dao.UserDao;
import chapter6to12.next.model.User;
import chapter6to12.next.mvc.AbstractController;
import chapter6to12.next.mvc.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ListUserController extends AbstractController {

    @Override
    protected ModelAndView doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        UserDao userDao = new UserDao();
        if (user == null) {
            return getJspView("redirect:/");
        } else {
            return getJspView("/user/list.jsp")
                    .addObject("users", userDao.findAll());
        }

    }
}
