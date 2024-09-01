package chapter6to12.next.web.Controller.qna;

import chapter6to12.next.dao.AnswerDao;
import chapter6to12.next.dao.QuestionDao;
import chapter6to12.next.mvc.AbstractController;
import chapter6to12.next.mvc.JspView;
import chapter6to12.next.mvc.View;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowController extends AbstractController {
    @Override
    public View doGet(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Long questionId = Long.parseLong(req.getParameter("questionId"));
        QuestionDao questionDao = new QuestionDao();
        AnswerDao answerDao = new AnswerDao();
        req.setAttribute("question", questionDao.findById(questionId));
        req.setAttribute("answers", answerDao.findAllByQuestionId(questionId));
        return new JspView("/qna/show.jsp");
    }
}
