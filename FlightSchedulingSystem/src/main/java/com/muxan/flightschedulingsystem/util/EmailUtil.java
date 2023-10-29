package com.muxan.flightschedulingsystem.util;

import java.util.regex.Pattern;

public final class EmailUtil {

    private EmailUtil(){}
    private static final String EMAIL_REGEX = "^(.+)@(\\S+)$";
    public static boolean patternMatches(String emailAddress) {
        return Pattern.compile(EMAIL_REGEX)
                .matcher(emailAddress)
                .matches();
    }
}
