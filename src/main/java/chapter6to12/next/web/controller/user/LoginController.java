package chapter6to12.next.web.controller.user;

import chapter6to12.next.dao.UserDao;
import chapter6to12.next.model.User;
import chapter6to12.next.mvc.AbstractController;
import chapter6to12.next.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginController extends AbstractController {

    @Override
    protected ModelAndView doPost(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        UserDao userDao = new UserDao();
        User findUser = userDao.findByUserId(userId);

        if (findUser != null && password.equals(findUser.getPassword())) {
            req.getSession().setAttribute("user", findUser);
        }
        return getJspView("redirect:/");
    }

    @Override
    protected ModelAndView doGet(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        return getJspView("/user/login.jsp");
    }
}
