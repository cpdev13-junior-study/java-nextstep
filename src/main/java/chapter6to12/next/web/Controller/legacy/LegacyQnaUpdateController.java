package chapter6to12.next.web.controller.legacy;

import chapter6to12.next.dao.JdbcQuestionDao;
import chapter6to12.next.model.Question;
import chapter6to12.next.model.User;
import chapter6to12.next.mvc.AbstractController;
import chapter6to12.next.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LegacyQnaUpdateController extends AbstractController {

    @Override
    protected ModelAndView doGet(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long questionId = Long.parseLong(request.getParameter("questionId"));
        JdbcQuestionDao questionDao = new JdbcQuestionDao();
        Question question = questionDao.findById(questionId);
        User user = (User) request.getSession().getAttribute("user");

        if (user != null && user.getName().equals(question.getWriter())) {
            return getJspView("/qna/update.jsp").addObject("question", question);
        }
        return getJspView("redirect:/");
    }

    @Override
    protected ModelAndView doPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long questionId = Long.parseLong(request.getParameter("questionId"));
        String title = request.getParameter("title");
        String contents = request.getParameter("contents");

        JdbcQuestionDao questionDao = new JdbcQuestionDao();
        Question question = questionDao.findById(questionId);

        User user = (User) request.getSession().getAttribute("user");
        if (user != null && user.getName().equals(question.getWriter())) {
            questionDao.update(new Question(question.getQuestionId(), question.getWriter(), title, contents, question.getCreatedDate(), question.getCountOfComment()));
        }

        return getJspView("redirect:/qna/show?questionId=" + questionId);
    }
}
