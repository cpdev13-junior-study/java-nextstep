package chapter6to12.next.mvc;

import chapter6to12.core.util.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AbstractController implements Controller {
    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Method method = Method.find(request.getMethod());
        if (method == Method.GET) {
            return doGet(request, response);
        } else {
            return doPost(request, response);
        }
    }

    protected ModelAndView doGet(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return null;
    }

    protected ModelAndView doPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return null;
    }

    protected ModelAndView getJsonView(){
        return new ModelAndView(new JsonView());
    }

    protected ModelAndView getJspView(String path){
        return new ModelAndView(new JspView(path));
    }
}
