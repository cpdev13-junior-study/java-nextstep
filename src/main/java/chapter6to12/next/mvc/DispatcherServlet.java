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
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        try {
            RequestMapping requestMapping = new RequestMapping();
            AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("chapter6to12.next.web.controller");
            annotationHandlerMapping.initialize();
            String requestURI = req.getRequestURI();

            HandlerExecution handler = annotationHandlerMapping.getHandler(req);

            logger.info("Method : {}, Request URI : {}", req.getMethod(), requestURI);

            Controller controller = requestMapping.getController(requestURI);

            if (controller != null) {
                render(controller.execute(req, resp), req, resp);
            } else if (handler != null) {
                render(handler.handle(req, resp), req, resp);
            } else {
                throw new RuntimeException("올바르지 않은 url입니다.");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void render(ModelAndView mav, HttpServletRequest request, HttpServletResponse response) throws Exception {
        View view = mav.getView();
        view.render(mav.getModel(), request, response);
    }

    static class RequestMapping {
        private Map<String, Controller> mapping = new HashMap<>();

        public RequestMapping() {
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
//            mapping.put("/", new HomeController());
        }

        public Controller getController(String url) {
            return mapping.get(url);
        }
    }
}
