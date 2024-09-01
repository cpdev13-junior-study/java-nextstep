package chapter6to12.next.web.Controller;

import chapter6to12.core.db.DataBase;
import chapter6to12.next.dao.UserDao;
import chapter6to12.next.model.User;
import chapter6to12.next.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UpdateUserController extends AbstractController {
    @Override
    protected String doGet(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String userId = req.getParameter("userId");
        UserDao userDao = new UserDao();
        User user = userDao.findByUserId(userId);
        req.setAttribute("user", user);
        return "/user/update.jsp";
    }

    @Override
    protected String doPost(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String userId = req.getParameter("userId");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String name = req.getParameter("name");

        User loginUser = (User) req.getSession().getAttribute("user");
        if (!loginUser.getUserId().equals(userId)) {
            throw new RuntimeException("수정할 수 없습니다.");
        }

        UserDao userDao = new UserDao();
        User findUser =  userDao.findByUserId(userId);
        findUser.setEmail(email);
        findUser.setPassword(password);
        findUser.setName(name);
        userDao.update(findUser);
        return "redirect:/user/list";
    }
}
