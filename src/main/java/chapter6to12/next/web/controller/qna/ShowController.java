package chapter6to12.next.web.controller.qna;

import chapter6to12.next.dao.JdbcAnswerDao;
import chapter6to12.next.dao.JdbcQuestionDao;
import chapter6to12.next.mvc.AbstractController;
import chapter6to12.next.mvc.ModelAndView;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowController extends AbstractController {
    @Override
    public ModelAndView doGet(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Long questionId = Long.parseLong(req.getParameter("questionId"));
        JdbcQuestionDao questionDao = new JdbcQuestionDao();
        JdbcAnswerDao answerDao = new JdbcAnswerDao();

        return getJspView("/qna/show.jsp")
                .addObject("question", questionDao.findById(questionId))
                .addObject("answers", answerDao.findAllByQuestionId(questionId));
    }
}
