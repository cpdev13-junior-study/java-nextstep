package chapter6to12.core.di.factory.example;

import chapter6to12.core.annotation.Controller;
import chapter6to12.core.annotation.Inject;
import chapter6to12.core.annotation.RequestMapping;
import chapter6to12.core.nmvc.AbstractNewController;
import chapter6to12.next.mvc.AbstractController;
import chapter6to12.next.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class QnaController extends AbstractNewController {
    private MyQnaService qnaService;

    @Inject
    public QnaController(MyQnaService qnaService) {
        this.qnaService = qnaService;
    }

    public MyQnaService getQnaService() {
        return qnaService;
    }

    @RequestMapping("/questions")
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return jspView("/qna/list.jsp");
    }
}
