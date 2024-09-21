package chapter6to12.core.nmvc;

import chapter6to12.core.annotation.Controller;
import com.google.common.collect.Maps;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    private final Reflections reflections;
    private final Map<Class<?>, Object> controllerMap = Maps.newHashMap();

    public ControllerScanner(final Object[] basePackages) {
        this.reflections = new Reflections(basePackages);
    }

    public Map<Class<?>, Object> getControllers() {
        try {
            final Set<Class<?>> findControllerClazzList = reflections.getTypesAnnotatedWith(Controller.class);

            for (Class<?> clazz : findControllerClazzList) {
                controllerMap.put(clazz, clazz.getDeclaredConstructor().newInstance());
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            log.error(e.getMessage());
        }
        return controllerMap;
    }
}