package chapter6to12.next.web.Controller;

import chapter6to12.next.dao.QuestionDao;
import chapter6to12.next.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HomeController extends AbstractController {

    @Override
    protected String doGet(HttpServletRequest request, HttpServletResponse response) throws Exception {
        QuestionDao questionDao = new QuestionDao();
        request.setAttribute("questions", questionDao.findAll());
        return "home.jsp";
    }
}
