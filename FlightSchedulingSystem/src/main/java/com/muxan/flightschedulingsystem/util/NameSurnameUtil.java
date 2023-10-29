package com.muxan.flightschedulingsystem.util;

public class NameSurnameUtil {
    private NameSurnameUtil() {}
    public static boolean containsInvalidCharacters(String input) {
        return !input.matches("^[[a-z] | [A-Z]]+$");
    }

}
