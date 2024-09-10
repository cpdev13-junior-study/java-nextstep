package chapter6to12.next.web.Controller.qna;

import chapter6to12.next.dao.QuestionDao;
import chapter6to12.next.model.Question;
import chapter6to12.next.mvc.AbstractController;
import chapter6to12.next.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddQnaController extends AbstractController {

    @Override
    protected ModelAndView doGet(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return getJspView("/qna/form.jsp");
    }

    @Override
    protected ModelAndView doPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String writer = request.getParameter("writer");
        String title = request.getParameter("title");
        String contents = request.getParameter("contents");
        QuestionDao questionDao = new QuestionDao();
        questionDao.insert(new Question(writer, title, contents));

        return getJspView("redirect:/");
    }
}
