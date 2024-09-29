package chapter6to12.next.web.controller.qna;

import chapter6to12.next.dao.AnswerDao;
import chapter6to12.next.dao.UserDao;
import chapter6to12.next.model.Answer;
import chapter6to12.next.model.CannotDeleteException;
import chapter6to12.next.model.User;
import chapter6to12.next.mvc.AbstractController;
import chapter6to12.next.mvc.ModelAndView;
import chapter6to12.next.service.QuestionService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class DeleteQuestionController extends AbstractController {

    private final QuestionService questionService;

    public DeleteQuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @Override
    protected ModelAndView doPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long questionId = Long.parseLong(request.getParameter("questionId"));
        User user = (User) request.getSession().getAttribute("user");
        boolean result = questionService.deleteQuestion(questionId, user);
        if(!result){
            throw new CannotDeleteException("삭제 불가능 합니다.");
        }
        return getJspView("redirect:/");
    }
}
