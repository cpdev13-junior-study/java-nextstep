package chapter6to12.core.ref;

import chapter6to12.next.model.Question;
import chapter6to12.next.model.User;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    public void newInstanceWithConstructorArgs() throws Exception {
        Class<User> clazz = User.class;
        logger.debug(clazz.getName());

        final Constructor<User> constructor = clazz.getDeclaredConstructor(String.class, String.class, String.class, String.class);
        final User user = constructor.newInstance("1", "password", "name", "hcsung@naver.com");
        System.out.println(user);
    }

    @Test
    public void privateFieldAccess() throws Exception {
        Class<Student> clazz = Student.class;
        logger.debug(clazz.getName());
        final Student student = new Student();

        final Field name = clazz.getDeclaredField("name");
        name.setAccessible(true);
        name.set(student,"hcsung");

        final Field age = clazz.getDeclaredField("age");
        age.setAccessible(true);
        age.setInt(student, 26);

        assertEquals("hcsung", student.getName());
        assertEquals(26, student.getAge());
    }
}