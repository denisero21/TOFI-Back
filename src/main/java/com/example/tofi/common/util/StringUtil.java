package com.example.tofi.common.util;

import static java.lang.String.format;

public class StringUtil {
    public static String maskPayerCard(String payerCard) {
        return format("**** %s", lastXChars(payerCard, 4));
    }

    public static String lastXChars(String str, int charCounter) {
        return str.length() <= charCounter ? str : str.substring(str.length() - charCounter);
    }

    public static String firstXChars(String str, int charCounter) {
        return str.substring(0, Math.min(str.length(), charCounter));
    }

    public static String formatExpDateFromMMYYToYYYYMM(String expDate) {
        String yyyy = format("20%s", lastXChars(expDate, 2));
        String mm = firstXChars(expDate, 2);
        return format("%s%s", yyyy, mm);
    }

    public static String formatExpDateFromMMYYToYYYY(String expDate) {
        return format("20%s", lastXChars(expDate, 2));
    }

    public static String formatExpDateFromMMYYToMM(String expDate) {
        return firstXChars(expDate, 2);
    }
}
