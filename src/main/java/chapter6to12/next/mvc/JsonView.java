package chapter6to12.next.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class JsonView implements View{
    @Override
    public void render(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.println(objectMapper.writeValueAsString(createModel(req)));
    }

    private Map<String, Object> createModel(HttpServletRequest req){
        Enumeration<String> names = req.getAttributeNames();
        HashMap<String, Object> model = new HashMap<>();
        while (names.hasMoreElements()){
            String name = names.nextElement();
            model.put(name, req.getAttribute(name));
        }
        return model;
    }
}
