package chapter2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CalculatorTest {

    private Calculator cal;


    @BeforeEach
    void before() {
        cal = new Calculator();
    }
    @Test
    void add() {
        Assertions.assertEquals(9, cal.add(6,3));
    }

    @Test
    void subtract() {
        Assertions.assertEquals(3, cal.substract(6,3));
    }
}
