package chapter2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class StringCalculatorTest {

    private StringCalculator cal;

    @BeforeEach
    void before() {
        cal = new StringCalculator();
    }

    @Test
    @DisplayName("실패: 음수 값을 계산")
    void addFail() {
        assertThatThrownBy(() -> cal.add("1,-2"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("음수는 계산이 불가능합니다.");

    }

    @Test
    @DisplayName("실패: 음수 값을 계산")
    void addWithCustomFail() {
        String str1 = "//;\n1;2;-3";
        String str2 = "//;\n-3";

        assertThatThrownBy(() -> cal.add(str1))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("음수는 계산이 불가능합니다.");

        assertThatThrownBy(() -> cal.add(str2))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("음수는 계산이 불가능합니다.");
    }


    @Test
    @DisplayName("성공: null 또는 공백 문자인 경우 0을 반환")
    void addNullOrEmpty() {
        int sum1 = cal.add("");
        int sum2 = cal.add(null);

        assertThat(sum1).isEqualTo(0);
        assertThat(sum2).isEqualTo(0);

    }

    @Test
    @DisplayName("성공: 커스텀 구분자를 사용하지 않고 문자열 합을 계산")
    void addSuccess() {

        int sum1 = cal.add("1,2");
        int sum2 = cal.add("1,2,3");
        int sum3 = cal.add("1,2:3");
        int sum4 = cal.add("");

        assertThat(sum1).isEqualTo(3);
        assertThat(sum2).isEqualTo(6);
        assertThat(sum3).isEqualTo(6);
        assertThat(sum4).isEqualTo(0);

    }

    @Test
    @DisplayName("성공: 커스텀 구분자를 사용해 문자열 합을 계산")
    void addWithCustomSuccess() {
        int sum1 = cal.add("//;\n1;2;3");
        int sum2 = cal.add("//;\n3");

        assertThat(sum1).isEqualTo(6);
        assertThat(sum2).isEqualTo(3);
    }

}