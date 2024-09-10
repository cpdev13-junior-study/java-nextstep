package chapter6to12.next.web.Controller;

import chapter6to12.next.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HomeController extends AbstractController {

    @Override
    protected String doGet(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "home.jsp";
    }
}
