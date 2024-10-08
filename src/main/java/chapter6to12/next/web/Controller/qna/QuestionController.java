package chapter6to12.next.web.controller.qna;

import chapter6to12.core.annotation.Controller;
import chapter6to12.core.annotation.Inject;
import chapter6to12.core.annotation.RequestMapping;
import chapter6to12.core.annotation.RequestMethod;
import chapter6to12.core.nmvc.AbstractNewController;
import chapter6to12.next.dao.AnswerDao;
import chapter6to12.next.dao.JdbcQuestionDao;
import chapter6to12.next.dao.QuestionDao;
import chapter6to12.next.model.CannotDeleteException;
import chapter6to12.next.model.Question;
import chapter6to12.next.model.User;
import chapter6to12.next.mvc.ModelAndView;
import chapter6to12.next.service.QuestionService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class QuestionController extends AbstractNewController {

    private final QuestionDao questionDao;
    private final AnswerDao answerDao;
    private final QuestionService questionService;

    @Inject
    public QuestionController(QuestionDao questionDao, AnswerDao answerDao,QuestionService questionService) {
        this.questionDao = questionDao;
        this.answerDao = answerDao;
        this.questionService  = questionService;
    }

    @RequestMapping("/qna/show")
    public ModelAndView showQuestion(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Long questionId = Long.parseLong(req.getParameter("questionId"));

        return jspView("/qna/show.jsp")
                .addObject("question", questionDao.findById(questionId))
                .addObject("answers", answerDao.findAllByQuestionId(questionId));
    }

    @RequestMapping(value = "/qna/delete", method = RequestMethod.POST)
    public ModelAndView deleteQuestion(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long questionId = Long.parseLong(request.getParameter("questionId"));
        User user = (User) request.getSession().getAttribute("user");
        boolean result = questionService.deleteQuestion(questionId, user);
        if(!result){
            throw new CannotDeleteException("삭제 불가능 합니다.");
        }
        return jspView("redirect:/");
    }

    @RequestMapping("/qna/update")
    public ModelAndView updateForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long questionId = Long.parseLong(request.getParameter("questionId"));
        Question question = questionDao.findById(questionId);
        User user = (User) request.getSession().getAttribute("user");

        if (user != null && user.getName().equals(question.getWriter())) {
            return jspView("/qna/update.jsp").addObject("question", question);
        }
        return jspView("redirect:/");
    }

    @RequestMapping(value = "/qna/update", method = RequestMethod.POST)
    public ModelAndView doPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long questionId = Long.parseLong(request.getParameter("questionId"));
        String title = request.getParameter("title");
        String contents = request.getParameter("contents");

        Question question = questionDao.findById(questionId);

        User user = (User) request.getSession().getAttribute("user");
        if (user != null && user.getName().equals(question.getWriter())) {
            questionDao.update(new Question(question.getQuestionId(), question.getWriter(), title, contents, question.getCreatedDate(), question.getCountOfComment()));
        }

        return jspView("redirect:/qna/show?questionId=" + questionId);
    }

    @RequestMapping(value = "/qna/register")
    public ModelAndView registerQuestionForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user =(User)request.getSession().getAttribute("user");
        if (user == null) {
            return jspView("redirect:/");
        }
        return jspView("/qna/form.jsp").addObject("user", user);
    }

    @RequestMapping(value = "/qna/register",method = RequestMethod.POST)
    public ModelAndView registerQuestion(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (request.getSession().getAttribute("user") == null) {
            return jspView("redirect:/");
        }
        String writer = request.getParameter("writer");
        String title = request.getParameter("title");
        String contents = request.getParameter("contents");
        questionDao.insert(new Question(writer, title, contents));

        return jspView("redirect:/");
    }
}
