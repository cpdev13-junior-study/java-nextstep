package chapter6to12.next.web.controller.legacy;

import chapter6to12.next.dao.JdbcAnswerDao;
import chapter6to12.next.dao.JdbcQuestionDao;
import chapter6to12.next.model.Answer;
import chapter6to12.next.model.Result;
import chapter6to12.next.mvc.AbstractController;
import chapter6to12.next.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LegacyDeleteAnswerController extends AbstractController {
    @Override
    protected ModelAndView doPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JdbcAnswerDao answerDao = new JdbcAnswerDao();
        JdbcQuestionDao questionDao = new JdbcQuestionDao();

        Answer answer = answerDao.findById(Long.parseLong(request.getParameter("answerId")));

        questionDao.decreaseCount(answer.getQuestionId());
        answerDao.delete(answer.getAnswerId());
        return getJsonView().addObject("result", Result.ok());
    }
}
