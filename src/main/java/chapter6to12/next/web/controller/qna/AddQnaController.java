package chapter6to12.next.web.controller.qna;

import chapter6to12.next.dao.JdbcQuestionDao;
import chapter6to12.next.model.Question;
import chapter6to12.next.model.User;
import chapter6to12.next.mvc.AbstractController;
import chapter6to12.next.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddQnaController extends AbstractController {

    @Override
    protected ModelAndView doGet(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user =(User)request.getSession().getAttribute("user");
        if (user == null) {
            return getJspView("redirect:/");
        }
        return getJspView("/qna/form.jsp").addObject("user", user);
    }

    @Override
    protected ModelAndView doPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (request.getSession().getAttribute("user") == null) {
            return getJspView("redirect:/");
        }
        String writer = request.getParameter("writer");
        String title = request.getParameter("title");
        String contents = request.getParameter("contents");
        JdbcQuestionDao questionDao = new JdbcQuestionDao();
        questionDao.insert(new Question(writer, title, contents));

        return getJspView("redirect:/");
    }
}
