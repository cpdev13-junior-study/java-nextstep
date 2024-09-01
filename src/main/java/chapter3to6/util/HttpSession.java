package chapter3to6.util;

import java.util.HashMap;
import java.util.Map;

public class HttpSession {
    private final Map<String, Object> sessionMap = new HashMap<>();
    private final String sessionId;

    public HttpSession(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getId() {
        return sessionId;
    }

    public void setAttribute(String name, Object value) {
        sessionMap.put(name, value);
    }

    public Object getAttribute(String name){
        return sessionMap.get(name);
    }

    public void removeAttribute(String name) {
        sessionMap.remove(name);
    }

    public void invalidate() {
        sessionMap.clear();
    }
}
