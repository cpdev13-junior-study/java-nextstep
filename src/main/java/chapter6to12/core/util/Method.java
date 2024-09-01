package chapter6to12.core.util;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Method {
    GET, POST;

    public static Method find(String name){
        return Arrays.stream(values())
                .filter(method -> method.name().equalsIgnoreCase(name))
                .findAny()
                .orElseThrow(() -> new RuntimeException("올바르지 않은 method입니다."));
    }
}
