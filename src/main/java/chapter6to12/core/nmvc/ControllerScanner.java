package chapter6to12.core.nmvc;

import chapter6to12.core.annotation.Controller;
import com.google.common.collect.Maps;
import org.reflections.Reflections;

import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    private final Reflections reflections;
    private final Map<Class<?>, Object> controllerMap = Maps.newHashMap();

    public ControllerScanner(final Object[] basePackages) {
        this.reflections = new Reflections(basePackages);
    }

    public Map<Class<?>, Object> getControllers() throws Exception {
        final Set<Class<?>> findControllerClazzList = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> clazz : findControllerClazzList) {
            controllerMap.put(clazz, clazz.getDeclaredConstructor().newInstance());
        }
        return controllerMap;
    }
}