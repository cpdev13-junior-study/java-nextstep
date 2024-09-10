package chapter6to12.next.web.Controller;

import chapter6to12.next.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutController extends AbstractController {

    @Override
    protected String doGet(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.getSession().invalidate();
        return "redirect:/";
    }
}
