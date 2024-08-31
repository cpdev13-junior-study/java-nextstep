package chapter3to6.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HttpSessions {

    private static final Map<String, HttpSession> sessionMap = new HashMap<>();

    public static HttpSession getSession(String id) {
        HttpSession session = sessionMap.get(id);
        if (session == null) {
            session = new HttpSession(id);
            sessionMap.put(id, session);
        }
        return session;
    }

}
