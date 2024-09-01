package chapter6to12.next.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface View {
    void render(HttpServletRequest req, HttpServletResponse resp) throws Exception;
}
