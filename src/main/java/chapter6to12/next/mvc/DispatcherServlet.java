package chapter6to12.next.mvc;

import chapter6to12.core.nmvc.AnnotationHandlerMapping;
import chapter6to12.next.dao.AnswerDao;
import chapter6to12.next.dao.JdbcAnswerDao;
import chapter6to12.next.dao.JdbcQuestionDao;
import chapter6to12.next.mvc.handler_adapter.AnnotationHandlerAdapter;
import chapter6to12.next.mvc.handler_adapter.HandlerAdapter;
import chapter6to12.next.mvc.handler_adapter.LegacyHandlerAdapter;
import chapter6to12.next.service.QuestionService;
import chapter6to12.next.web.controller.qna.*;
import chapter6to12.next.web.controller.user.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private List<HandlerMapping> handlerMappingList = new ArrayList<>();
    private List<HandlerAdapter> handlerAdapterList = new ArrayList<>();

    @Override
    public void init() throws ServletException {
        LegacyRequestMapping legacyRequestMapping = new LegacyRequestMapping();
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("chapter6to12.next");
        annotationHandlerMapping.initialize();
        handlerMappingList.add(annotationHandlerMapping);
        handlerMappingList.add(legacyRequestMapping);

        handlerAdapterList.add(new LegacyHandlerAdapter());
        handlerAdapterList.add(new AnnotationHandlerAdapter());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String requestURI = req.getRequestURI();
            logger.info("Method : {}, Request URI : {}", req.getMethod(), requestURI);

            ModelAndView mav = handle(getHandler(req), req, resp);
            render(mav, req, resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ModelAndView handle(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception {
        for (HandlerAdapter adapter : handlerAdapterList) {
            if (adapter.isSupport(handler)) {
                return adapter.handle(handler, request, response);
            }
        }
        return null;
    }

    private Object getHandler(HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappingList) {
            Object handler = handlerMapping.getHandler(request);
            if (handler != null) return handler;
        }
        return null;
    }

    private void render(ModelAndView mav, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (mav == null) {
            throw new RuntimeException("url에 올바른 handler를 찾을 수 없습니다.");
        }
        View view = mav.getView();
        view.render(mav.getModel(), request, response);
    }

    static class LegacyRequestMapping implements HandlerMapping {
        private final Map<String, Controller> mapping = new HashMap<>();

        public LegacyRequestMapping() {
            JdbcQuestionDao jdbcQuestionDao = new JdbcQuestionDao();
            AnswerDao answerDao = new JdbcAnswerDao();
            QuestionService questionService = new QuestionService(jdbcQuestionDao,answerDao);

//            mapping.put("/qna/register", new AddQnaController());
            mapping.put("/api/qna/addAnswer", new AddAnswerController());
            mapping.put("/api/qna/deleteAnswer", new DeleteAnswerController());
            mapping.put("/api/qna/delete", new DeleteQuestionApiController(questionService));
            mapping.put("/api/qna/list", new QnaListController());
        }

        @Override
        public Object getHandler(HttpServletRequest request) {
            return mapping.get(request.getRequestURI());
        }
    }
}
