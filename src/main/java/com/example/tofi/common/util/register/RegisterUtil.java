package com.example.tofi.common.util.register;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;


public class RegisterUtil {
    public static Pair<String, Resource> generateRegisterNameWithResourceRegister(String fileName, String[] registerData) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            for (String data : registerData) {
                bos.write(data.getBytes());
            }

            return new ImmutablePair<>(fileName, setByteArrayToResource(bos));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create register, filename:" + fileName);
        }
    }

    public static Resource setByteArrayToResource(ByteArrayOutputStream bos) {
        try {
            return new ByteArrayResource(bos.toByteArray());
        } finally {
            bos.reset();
        }
    }

    /**
     * Register file name for halic bank:92900927.txt
     * 9 - any number but should not change
     * 290 - 3 digits between 100 and 999
     * 0927 - 09 month 27 day
     */

    public static String generateRegisterFileName() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMdd");
        String formattedDateTime = formatter.format(LocalDateTime.now());
        return "9" + new Random().nextInt(100, 999) + formattedDateTime + ".txt";
    }
}