package chapter6to12.next.web.Controller;

import chapter6to12.core.db.DataBase;
import chapter6to12.next.model.User;
import chapter6to12.next.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginController extends AbstractController {

    @Override
    protected String doPost(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        User findUser = DataBase.findUserById(userId);

        if (findUser != null && password.equals(findUser.getPassword())) {
            req.getSession().setAttribute("user", findUser);
        }
        return "redirect:/index.jsp";
    }

    @Override
    protected String doGet(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        return "/user/login.jsp";
    }
}
