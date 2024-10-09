package chapter6to12.next.web.controller;

import chapter6to12.core.annotation.Controller;
import chapter6to12.core.annotation.RequestMapping;
import chapter6to12.core.nmvc.AbstractNewController;
import chapter6to12.next.dao.JdbcQuestionDao;
import chapter6to12.next.mvc.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class HomeController extends AbstractNewController {
    @RequestMapping("/")
    public ModelAndView getHomeView(HttpServletRequest request, HttpServletResponse response){
        JdbcQuestionDao questionDao = new JdbcQuestionDao();
        return jspView("home.jsp")
                .addObject("questions", questionDao.findAll());
    }
}
