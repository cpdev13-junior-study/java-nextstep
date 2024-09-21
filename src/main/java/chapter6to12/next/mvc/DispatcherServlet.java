package chapter6to12.next.mvc;

import chapter6to12.core.nmvc.AnnotationHandlerMapping;
import chapter6to12.core.nmvc.HandlerExecution;
import chapter6to12.next.web.controller.qna.*;
import chapter6to12.next.web.controller.user.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String requestURI = req.getRequestURI();
            logger.info("Method : {}, Request URI : {}", req.getMethod(), requestURI);

            createHandlerMapping();
            Object handler = getHandler(req);

            ModelAndView mav = null;
            if(handler instanceof Controller){
                mav = ((Controller) handler).execute(req, resp);
            } else if (handler instanceof HandlerExecution) {
                mav = ((HandlerExecution) handler).handle(req, resp);
            }  else {
                throw new RuntimeException("url에 올바른 handler를 찾을 수 없습니다.");
            }
            render(mav,req,resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object getHandler(HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappingList) {
            Object handler = handlerMapping.getHandler(request);
            if(handler!= null) return handler;
        }
        return null;
    }

    private void createHandlerMapping() throws Exception {
        LegacyRequestMapping legacyRequestMapping = new LegacyRequestMapping();
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("chapter6to12.next.web.controller");
        annotationHandlerMapping.initialize();
        handlerMappingList.add(annotationHandlerMapping);
        handlerMappingList.add(legacyRequestMapping);
    }

    private void render(ModelAndView mav, HttpServletRequest request, HttpServletResponse response) throws Exception {
        View view = mav.getView();
        view.render(mav.getModel(), request, response);
    }

    static class LegacyRequestMapping implements HandlerMapping {
        private final Map<String, Controller> mapping = new HashMap<>();

        public LegacyRequestMapping() {
            mapping.put("/user/create", new CreateUserController());
            mapping.put("/user/list", new ListUserController());
            mapping.put("/user/login", new LoginController());
            mapping.put("/user/logout", new LogoutController());
            mapping.put("/user/update", new UpdateUserController());
            mapping.put("/qna/show", new ShowController());
            mapping.put("/qna/form", new AddQnaController());
            mapping.put("/api/qna/addAnswer", new AddAnswerController());
            mapping.put("/api/qna/deleteAnswer", new DeleteAnswerController());
            mapping.put("/api/qna/delete", new DeleteQuestionApiController());
            mapping.put("/qna/delete", new DeleteQuestionController());
            mapping.put("/qna/update", new QnaUpdateController());
            mapping.put("/api/qna/list", new QnaListController());
        }

        @Override
        public Object getHandler(HttpServletRequest request) {
            return mapping.get(request.getRequestURI());
        }
    }
}
