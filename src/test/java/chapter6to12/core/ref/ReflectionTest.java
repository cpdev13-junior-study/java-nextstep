package chapter6to12.core.ref;

import chapter6to12.next.model.Question;
import chapter6to12.next.model.User;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());

        // fields
        Arrays.stream(clazz.getDeclaredFields())
                .forEach(System.out::println);
        // constructors
        Arrays.stream(clazz.getDeclaredConstructors())
                .forEach(System.out::println);
        // methods
        Arrays.stream(clazz.getDeclaredMethods())
                .forEach(System.out::println);
    }

    @Test
    public void newInstanceWithConstructorArgs() {
        Class<User> clazz = User.class;
        logger.debug(clazz.getName());
    }

    @Test
    public void privateFieldAccess() {
        Class<Student> clazz = Student.class;
        logger.debug(clazz.getName());
    }
}