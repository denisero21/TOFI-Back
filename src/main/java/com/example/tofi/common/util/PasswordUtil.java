package com.example.tofi.common.util;


import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class PasswordUtil {
    /**
     * Minimum eight characters, at least one uppercase letter, one lowercase letter,
     * one number and one special character
     * Random of two values of each constraint below:
     */
    private static final String UPPER_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER_CHARS = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARS = "!«#$&‘()*+,-./:;<=>?@[\\]^_{|}~";
    private static final int UPPER_CHARS_COUNT = 3;
    private static final int LOWER_CHARS_COUNT = 2;
    private static final int DIGITS_COUNT = 2;
    private static final int SPECIAL_CHARS_COUNT = 1;

    public static String generateOtpPassword() {
        String s = String.join("", RandomStringUtils.random(UPPER_CHARS_COUNT, UPPER_CHARS),
                RandomStringUtils.random(LOWER_CHARS_COUNT, LOWER_CHARS),
                RandomStringUtils.random(DIGITS_COUNT, DIGITS),
                RandomStringUtils.random(SPECIAL_CHARS_COUNT, SPECIAL_CHARS));

        List<Character> chars = s.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
        Collections.shuffle(chars);
        return StringUtils.join(chars.toArray());
    }
}
