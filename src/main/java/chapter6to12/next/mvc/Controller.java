package chapter6to12.next.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Controller {
    View execute(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
