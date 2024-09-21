package chapter6to12.core.ref;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class Junit3TestRunner {
    @Test
    public void run() throws Exception {
        Class<Junit3Test> clazz = Junit3Test.class;
        final Junit3Test instance = clazz.getDeclaredConstructor().newInstance();
        final List<Method> methodList = Arrays.stream(clazz.getMethods())
                .filter(method -> method.getName().startsWith("test"))
                .toList();
        for (Method method : methodList) {
            method.invoke(instance);
        }
    }
}