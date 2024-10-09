package chapter6to12.next.mvc;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Set;

public class JspView implements View {

    private final static String REDIRECT_PREFIX = "redirect:";
    private final String view;

    public JspView(String view) {
        if (view == null) {
            throw new NullPointerException("viewName이 null입니다.");
        }
        this.view = view;
    }

    @Override
    public void render(Map<String, Object> model, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (view.startsWith(REDIRECT_PREFIX)) {
            resp.sendRedirect(view.substring(REDIRECT_PREFIX.length()));
        } else {
            Set<String> keyList = model.keySet();
            for (String key : keyList) {
                req.setAttribute(key, model.get(key));
            }
            req.getRequestDispatcher(view).forward(req, resp);
        }
    }
}
