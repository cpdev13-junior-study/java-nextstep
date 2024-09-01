package chapter6to12.next.web.Controller.user;

import chapter6to12.next.mvc.AbstractController;
import chapter6to12.next.mvc.JspView;
import chapter6to12.next.mvc.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutController extends AbstractController {

    @Override
    protected View doGet(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.getSession().invalidate();
        return new JspView("redirect:/");
    }
}
