package chapter6to12.next.web.controller.qna;

import chapter6to12.next.dao.AnswerDao;
import chapter6to12.next.dao.QuestionDao;
import chapter6to12.next.model.Answer;
import chapter6to12.next.model.Result;
import chapter6to12.next.mvc.AbstractController;
import chapter6to12.next.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteAnswerController extends AbstractController {
    @Override
    protected ModelAndView doPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
        AnswerDao answerDao = new AnswerDao();
        QuestionDao questionDao = new QuestionDao();

        Answer answer = answerDao.findById(Long.parseLong(request.getParameter("answerId")));

        questionDao.decreaseCount(answer.getQuestionId());
        answerDao.delete(answer.getAnswerId());
        return getJsonView().addObject("result", Result.ok());
    }
}
