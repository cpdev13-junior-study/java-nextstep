package chapter6to12.next.web.Controller.qna;

import chapter6to12.next.dao.AnswerDao;
import chapter6to12.next.model.Answer;
import chapter6to12.next.mvc.AbstractController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class AnswerController extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(AnswerController.class);

    @Override
    protected String doPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Answer answer = new Answer(request.getParameter("writer"), request.getParameter("contents"), Long.parseLong(request.getParameter("questionId")));
        log.debug("answer : {}", answer);

        AnswerDao answerDao = new AnswerDao();
        Answer savedAnswer = answerDao.insert(answer);
        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println(objectMapper.writeValueAsString(savedAnswer));
        return null;
    }
}
