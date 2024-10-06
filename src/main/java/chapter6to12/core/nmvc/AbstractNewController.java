package chapter6to12.core.nmvc;

import chapter6to12.next.mvc.JsonView;
import chapter6to12.next.mvc.JspView;
import chapter6to12.next.mvc.ModelAndView;

public class AbstractNewController {
    protected ModelAndView jspView(String forwardUrl) {
        return new ModelAndView(new JspView(forwardUrl));
    }

    protected ModelAndView jsonView() {
        return new ModelAndView(new JsonView());
    }
}
