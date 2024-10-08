package chapter6to12.next.web.controller.user;

import chapter6to12.core.annotation.Controller;
import chapter6to12.core.annotation.RequestMapping;
import chapter6to12.core.nmvc.AbstractNewController;
import chapter6to12.next.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LogoutController extends AbstractNewController {

    @RequestMapping("/user/logout")
    public ModelAndView logout(HttpServletRequest req, HttpServletResponse resp) {
        req.getSession().invalidate();
        return jspView("redirect:/");
    }
}
