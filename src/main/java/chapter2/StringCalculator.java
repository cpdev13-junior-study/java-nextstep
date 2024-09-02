package chapter2;

import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringCalculator {

    private final List<String> DEFAULT_DELIMITER_LIST = List.of(",", ":");

    private int sum(String[] values) {
        int sum = 0;
        for (String s : values) {
            int num = Integer.parseInt(s);
            if (num < 0) throw new RuntimeException("음수는 계산이 불가능합니다.");
            sum += num;
        }
        return sum;
    }

    public int add(String str) {
        if (str == null || str.isEmpty()) {
            return 0;
        }

        return sum(split(str));
    }

    private String[] split(String str) {
        Matcher matcher = Pattern.compile("//(.)\n(.*)").matcher(str);
        String customDelimiter = null;
        if (matcher.find()) {
            customDelimiter = matcher.group(1);
            str = matcher.group(2);
        }
        return str.split(createDelimiterRegex(customDelimiter));
    }

    private String createDelimiterRegex(String customDelimiter) {
        StringJoiner joiner = new StringJoiner("|");
        for (String seprator : DEFAULT_DELIMITER_LIST) {
            joiner.add(seprator);
        }
        if (customDelimiter != null && !DEFAULT_DELIMITER_LIST.contains(customDelimiter)) {
            joiner.add(customDelimiter);
        }
        return joiner.toString();
    }

}
