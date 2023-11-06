package com.example.tofi.common.util;

import jakarta.servlet.http.HttpServletRequest;

public class AuthContextUtil {
    public static String getCurrentUserEmail(HttpServletRequest request) {
        return request.getHeader("username");
    }
}