package com.example.tofi.common.web.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Arrays;

@Setter
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorInfo {

    String method;
    String url;
    String queryParams;
    String ip;
    String user;
    ErrorType type;
    String error;
    String[] error_description;

    public ErrorInfo(String method, CharSequence url, String queryParams, String ip, String user,
                     ErrorType type, String error, String... error_description) {
        this.method = method;
        this.url = url.toString();
        this.queryParams = queryParams;
        this.ip = ip;
        this.user = user;
        this.type = type;
        this.error = error;
        this.error_description = Arrays.stream(error_description).distinct().toArray(String[]::new);
    }
}