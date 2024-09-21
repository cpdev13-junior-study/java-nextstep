package chapter6to12.core.nmvc;

import chapter6to12.core.annotation.RequestMapping;
import chapter6to12.core.annotation.RequestMethod;
import com.google.common.collect.Maps;
import org.reflections.ReflectionUtils;


import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping {
    private Object[] basePackage;

    private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    @SuppressWarnings("unchecked")
    public void initialize() throws Exception {
        final ControllerScanner scanner = new ControllerScanner(basePackage);
        final Map<Class<?>, Object> controllers = scanner.getControllers();
        for (Map.Entry<Class<?>, Object> scanInfo : controllers.entrySet()) {
            final Set<Method> findMethods = ReflectionUtils.getAllMethods(scanInfo.getKey(), ReflectionUtils.withAnnotation(RequestMapping.class));
            for (Method findMethod : findMethods) {
                final RequestMapping annotation = findMethod.getAnnotation(RequestMapping.class);
                final HandlerKey handlerKey = createHandlerKey(annotation);
                handlerExecutions.put(handlerKey, new HandlerExecution(scanInfo.getValue(), findMethod));
            }
        }

    }

    private HandlerKey createHandlerKey(RequestMapping rm) {
        return new HandlerKey(rm.value(), rm.method());
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, rm));
    }
}