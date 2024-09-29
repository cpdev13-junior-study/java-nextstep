package chapter6to12.next.web.controller.qna;

import chapter6to12.next.model.User;
import chapter6to12.next.mvc.AbstractController;
import chapter6to12.next.mvc.ModelAndView;
import chapter6to12.next.service.QuestionService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteQuestionApiController extends AbstractController {

    private final QuestionService questionService;

    public DeleteQuestionApiController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @Override
    protected ModelAndView doPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long questionId = Long.parseLong(request.getParameter("questionId"));
        User user = (User) request.getSession().getAttribute("user");
        boolean result = questionService.deleteQuestion(questionId, user);

        return getJsonView().addObject("result", result);
    }
}
