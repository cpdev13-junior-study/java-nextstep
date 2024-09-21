package chapter6to12.next.web.controller.user;

import chapter6to12.next.mvc.AbstractController;
import chapter6to12.next.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutController extends AbstractController {

    @Override
    protected ModelAndView doGet(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.getSession().invalidate();
        return getJspView("redirect:/");
    }
}
