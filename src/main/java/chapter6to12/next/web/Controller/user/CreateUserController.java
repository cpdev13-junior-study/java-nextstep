package chapter6to12.next.web.Controller.user;

import chapter6to12.next.dao.UserDao;
import chapter6to12.next.model.User;
import chapter6to12.next.mvc.AbstractController;
import chapter6to12.next.mvc.JspView;
import chapter6to12.next.mvc.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CreateUserController extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(CreateUserController.class);

    @Override
    protected View doPost(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        User user = new User(req.getParameter("userId"), req.getParameter("password"), req.getParameter("name"),
                req.getParameter("email"));
        log.debug("user : {}", user);

        UserDao userDao = new UserDao();
        userDao.insert(user);
        return new JspView("redirect:/");
    }

    @Override
    protected View doGet(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        return new JspView("/user/form.jsp");
    }
}
