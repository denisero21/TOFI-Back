package com.example.tofi.common.util;

import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@UtilityClass
public class DateUtil {
    public static LocalDateTime convertUnixTimeToLocalDateTime(String unixTime) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(Long.parseLong(unixTime)), ZoneId.systemDefault());
    }

    public static LocalDateTime convertUnixTimeToLocalDateTime(Long unixTime) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(unixTime), ZoneId.systemDefault());
    }

    public static Long convertLocalDateTimeToUnixTime(LocalDateTime localDateTime) {
        return localDateTime
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
    }
}