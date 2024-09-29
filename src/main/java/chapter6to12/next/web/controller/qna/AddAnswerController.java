package chapter6to12.next.web.controller.qna;

import chapter6to12.next.dao.JdbcAnswerDao;
import chapter6to12.next.dao.JdbcQuestionDao;
import chapter6to12.next.model.Answer;
import chapter6to12.next.mvc.AbstractController;
import chapter6to12.next.mvc.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddAnswerController extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(AddAnswerController.class);

    @Override
    protected ModelAndView doPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Answer answer = new Answer(request.getParameter("writer"), request.getParameter("contents"), Long.parseLong(request.getParameter("questionId")));
        log.debug("answer : {}", answer);

        JdbcAnswerDao answerDao = new JdbcAnswerDao();
        JdbcQuestionDao questionDao = new JdbcQuestionDao();
        questionDao.increaseCount(answer.getQuestionId());

        Answer savedAnswer = answerDao.insert(answer);

        return getJsonView().addObject("answer", savedAnswer);
    }
}
