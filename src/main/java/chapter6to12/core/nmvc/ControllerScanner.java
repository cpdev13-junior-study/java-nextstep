package chapter6to12.core.nmvc;

import chapter6to12.core.annotation.Controller;
import chapter6to12.core.annotation.Repository;
import chapter6to12.core.annotation.Service;
import chapter6to12.core.di.factory.BeanFactory;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

public class ControllerScanner {

    private final Reflections reflections;
    private final Map<Class<?>, Object> controllerMap = Maps.newHashMap();


    public ControllerScanner(final Object[] basePackages) {
        this.reflections = new Reflections(basePackages);
    }

    public Map<Class<?>, Object> getControllers() {

        BeanFactory beanFactory = new BeanFactory(getTypesAnnotatedWith(Controller.class, Service.class, Repository.class));
        beanFactory.initialize();

        final Set<Class<?>> findControllerClazzList = getTypesAnnotatedWith(Controller.class);
        for (Class<?> clazz : findControllerClazzList) {
            controllerMap.put(clazz, beanFactory.getBean(clazz));
        }

        return controllerMap;
    }

    private Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        return beans;
    }
}