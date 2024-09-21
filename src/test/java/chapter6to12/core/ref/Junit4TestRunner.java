package chapter6to12.core.ref;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class Junit4TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit4Test> clazz = Junit4Test.class;
        final Junit4Test instance = clazz.getDeclaredConstructor().newInstance();
        final List<Method> methodList = Arrays.stream(clazz.getMethods())
                .filter(method -> method.isAnnotationPresent(MyTest.class))
                .toList();

        for (Method method : methodList) {
            method.invoke(instance);
        }
    }
}