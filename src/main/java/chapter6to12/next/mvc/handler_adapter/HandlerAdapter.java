package chapter6to12.next.mvc.handler_adapter;

import chapter6to12.next.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerAdapter {

    boolean isSupport(Object handler);

    ModelAndView handle(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
