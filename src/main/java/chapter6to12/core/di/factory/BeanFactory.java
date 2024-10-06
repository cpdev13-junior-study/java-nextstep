package chapter6to12.core.di.factory;

import chapter6to12.core.annotation.Inject;
import com.google.common.collect.Maps;
import org.apache.el.util.ReflectionUtil;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class BeanFactory {
    private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

    private Set<Class<?>> preInstanticateBeans;

    private Map<Class<?>, Object> beans = Maps.newHashMap();

    public BeanFactory(Set<Class<?>> preInstanticateBeans) {
        this.preInstanticateBeans = preInstanticateBeans;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.get(requiredType);
    }

    public void initialize() {
        for (Class<?> preInstanticateBean : preInstanticateBeans) {
            instantiateClass(preInstanticateBean);
        }
    }

    private Object instantiateClass(Class<?> clazz) {
        if (beans.containsKey(clazz)) {
            return beans.get(clazz);
        }

        try {
            Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(clazz, preInstanticateBeans);
            Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(concreteClass);
            // 기본 생성자
            if (injectedConstructor == null) {
                return saveBean(clazz, concreteClass.getDeclaredConstructor().newInstance());
            }
            // @Inject 생성자
            return saveBean(clazz, instantiateConstructor(injectedConstructor));

        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException("빈 생성에 실패했습니다.", e);
        }
    }

    private Object instantiateConstructor(Constructor<?> constructor) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] parameterObjects = Arrays.stream(parameterTypes)
                .map(type -> instantiateClass(type))
                .toArray();
        return constructor.newInstance(parameterObjects);
    }

    private Object saveBean(Class<?> clazz, Object bean) {
        beans.put(clazz, bean);
        return beans.get(clazz);
    }
}
