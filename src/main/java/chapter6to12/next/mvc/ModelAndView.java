package chapter6to12.next.mvc;

import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ModelAndView {

    @Getter
    private final View view;
    private final Map<String, Object> model = new HashMap<>();

    public ModelAndView(View view) {
        this.view = view;
    }

    public ModelAndView addObject(String name, Object value){
        model.put(name, value);
        return this;
    }

    public Map<String, Object> getModel(){
        return Collections.unmodifiableMap(model);
    }
}
