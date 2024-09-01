package chapter6to12.next.web.Controller.qna;

import chapter6to12.next.dao.AnswerDao;
import chapter6to12.next.model.Result;
import chapter6to12.next.mvc.AbstractController;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class DeleteAnswerController extends AbstractController {
    @Override
    protected String doPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long answerId = Long.parseLong(request.getParameter("answerId"));
        AnswerDao answerDao = new AnswerDao();

        answerDao.delete(answerId);

        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(mapper.writeValueAsString(Result.ok()));
        return null;
    }
}
