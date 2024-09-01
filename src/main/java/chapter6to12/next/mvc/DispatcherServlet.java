package chapter6to12.next.mvc;

import chapter6to12.next.web.Controller.*;
import chapter6to12.next.web.Controller.qna.ShowController;
import chapter6to12.next.web.Controller.user.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private final static String REDIRECT_PREFIX = "redirect:";

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestMapping requestMapping = new RequestMapping();
        String requestURI = req.getRequestURI();

        logger.info("Method : {}, Request URI : {}", req.getMethod(), requestURI);

        Controller controller = requestMapping.getController(requestURI);
        if (controller == null) {
            throw new RuntimeException("올바르지 않은 url입니다.");
        }
        try {
            String viewName = controller.execute(req, resp);
            send(viewName, req, resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void send(String viewName, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (viewName.startsWith(REDIRECT_PREFIX)) {
            resp.sendRedirect(viewName.substring(REDIRECT_PREFIX.length()));
        } else {
            req.getRequestDispatcher(viewName).forward(req, resp);
        }
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
            mapping.put("/", new HomeController());
        }

        public Controller getController(String url) {
            return mapping.get(url);
        }
    }
}
