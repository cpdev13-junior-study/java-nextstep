package chapter6to12.next.web.Controller.qna;

import chapter6to12.next.dao.AnswerDao;
import chapter6to12.next.model.Answer;
import chapter6to12.next.mvc.AbstractController;
import chapter6to12.next.mvc.JsonView;
import chapter6to12.next.mvc.View;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class AddAnswerController extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(AddAnswerController.class);

    @Override
    protected View doPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Answer answer = new Answer(request.getParameter("writer"), request.getParameter("contents"), Long.parseLong(request.getParameter("questionId")));
        log.debug("answer : {}", answer);

        AnswerDao answerDao = new AnswerDao();
        Answer savedAnswer = answerDao.insert(answer);

        request.setAttribute("answer", savedAnswer);
        return new JsonView();
    }
}
