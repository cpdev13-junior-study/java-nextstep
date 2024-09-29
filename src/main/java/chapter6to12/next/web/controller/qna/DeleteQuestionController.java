package chapter6to12.next.web.controller.qna;

import chapter6to12.next.mvc.AbstractController;
import chapter6to12.next.mvc.ModelAndView;
import chapter6to12.next.service.QuestionService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteQuestionController extends AbstractController {

    private final QuestionService questionService;

    public DeleteQuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @Override
    protected ModelAndView doPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long questionId = Long.parseLong(request.getParameter("questionId"));
        boolean result = questionService.deleteQuestion(questionId);
        return getJspView("redirect:/");
    }
}
