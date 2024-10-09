package chapter6to12.next.web.controller.qna;

import chapter6to12.core.annotation.Controller;
import chapter6to12.core.annotation.Inject;
import chapter6to12.core.annotation.RequestMapping;
import chapter6to12.core.annotation.RequestMethod;
import chapter6to12.core.nmvc.AbstractNewController;
import chapter6to12.next.dao.AnswerDao;
import chapter6to12.next.dao.QuestionDao;
import chapter6to12.next.model.Answer;
import chapter6to12.next.model.Result;
import chapter6to12.next.mvc.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ApiAnswerController extends AbstractNewController {

    private static final Logger log = LoggerFactory.getLogger(ApiAnswerController.class);
    private final AnswerDao answerDao;
    private final QuestionDao questionDao;

    @Inject
    public ApiAnswerController(AnswerDao answerDao, QuestionDao questionDao) {
        this.answerDao = answerDao;
        this.questionDao = questionDao;
    }

    @RequestMapping(value = "/api/qna/addAnswer",method = RequestMethod.POST)
    public ModelAndView addAnswer(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Answer answer = new Answer(request.getParameter("writer"), request.getParameter("contents"), Long.parseLong(request.getParameter("questionId")));
        log.debug("answer : {}", answer);

        questionDao.increaseCount(answer.getQuestionId());
        Answer savedAnswer = answerDao.insert(answer);
        return jsonView().addObject("answer", savedAnswer);
    }

    @RequestMapping(value = "/api/qna/deleteAnswer",method = RequestMethod.POST)
    public ModelAndView doPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Answer answer = answerDao.findById(Long.parseLong(request.getParameter("answerId")));

        questionDao.decreaseCount(answer.getQuestionId());
        answerDao.delete(answer.getAnswerId());
        return jsonView().addObject("result", Result.ok());
    }
}
