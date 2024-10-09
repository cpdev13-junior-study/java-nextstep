package chapter6to12.next.web.controller.qna;

import chapter6to12.core.annotation.Controller;
import chapter6to12.core.annotation.Inject;
import chapter6to12.core.annotation.RequestMapping;
import chapter6to12.core.annotation.RequestMethod;
import chapter6to12.core.nmvc.AbstractNewController;
import chapter6to12.next.dao.JdbcQuestionDao;
import chapter6to12.next.model.User;
import chapter6to12.next.mvc.ModelAndView;
import chapter6to12.next.service.QuestionService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ApiQuestionController extends AbstractNewController {

    private final QuestionService questionService;

    @Inject
    public ApiQuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @RequestMapping(value = "/api/qna/delete", method = RequestMethod.POST)
    public ModelAndView deleteQuestion(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long questionId = Long.parseLong(request.getParameter("questionId"));
        User user = (User) request.getSession().getAttribute("user");
        boolean result = questionService.deleteQuestion(questionId, user);

        return jsonView().addObject("result", result);
    }

    @RequestMapping("/api/qna/list")
    public ModelAndView questionList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JdbcQuestionDao questionDao = new JdbcQuestionDao();
        return jsonView().addObject("questions",questionDao.findAll());
    }
}
