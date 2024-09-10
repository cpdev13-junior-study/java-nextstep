package chapter3to6.webserver;

import chapter3to6.controller.Controller;
import chapter3to6.controller.CreateUserController;
import chapter3to6.controller.ListUserController;
import chapter3to6.controller.LoginController;

import java.util.HashMap;
import java.util.Map;

public class RequestMapping {

    private Map<String, Controller> controllerMap;

    public RequestMapping() {
        this.controllerMap = new HashMap<>();
        controllerMap.put("/user/list", new ListUserController());
        controllerMap.put("/user/create", new CreateUserController());
        controllerMap.put("/user/login", new LoginController());
    }

    public Controller getController(String key){
        return controllerMap.get(key);
    }
}
