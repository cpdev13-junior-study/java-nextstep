package chapter6to12.next.web.controller.user;


import chapter6to12.core.annotation.Controller;
import chapter6to12.core.annotation.Inject;
import chapter6to12.core.annotation.RequestMapping;
import chapter6to12.core.annotation.RequestMethod;
import chapter6to12.core.nmvc.AbstractNewController;
import chapter6to12.next.dao.UserDao;
import chapter6to12.next.model.User;
import chapter6to12.next.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController extends AbstractNewController {

    private final UserDao userDao;

    @Inject
    public LoginController(UserDao userDao) {
        this.userDao = userDao;
    }

    @RequestMapping("/user/login")
    public ModelAndView getLoginForm(HttpServletRequest req, HttpServletResponse resp) {
        return jspView("/user/login.jsp");
    }

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public ModelAndView Login(HttpServletRequest req, HttpServletResponse resp) {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        User findUser = userDao.findByUserId(userId);

        if (findUser != null && password.equals(findUser.getPassword())) {
            req.getSession().setAttribute("user", findUser);
        }
        return jspView("redirect:/");
    }
}
