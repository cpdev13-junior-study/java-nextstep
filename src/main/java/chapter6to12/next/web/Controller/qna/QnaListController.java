package chapter6to12.next.web.Controller.qna;

import chapter6to12.next.dao.QuestionDao;
import chapter6to12.next.mvc.AbstractController;
import chapter6to12.next.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class QnaListController extends AbstractController {
    @Override
    protected ModelAndView doGet(HttpServletRequest request, HttpServletResponse response) throws Exception {
        QuestionDao questionDao = new QuestionDao();
        return getJsonView().addObject("questions",questionDao.findAll());
    }
}
