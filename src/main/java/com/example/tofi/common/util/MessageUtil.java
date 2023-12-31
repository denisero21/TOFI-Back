package com.example.tofi.common.util;

import com.example.tofi.common.web.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class MessageUtil {

    private final MessageSource messageSource;

    @Autowired
    public MessageUtil(@Qualifier("messageSource") MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String code, Locale locale, String... args) {
        return messageSource.getMessage(code, args, locale);
    }

    public String getMessage(String code, String... args) {
        return getMessage(code, LocaleContextHolder.getLocale(), args);
    }

    public String getMessage(String code) {
        return getMessage(code, LocaleContextHolder.getLocale(), (String) null);
    }

    public List<String> getMessages(List<String> codes) {
        return codes.stream().map(this::getMessage).toList();
    }

    public String getMessage(ApplicationException appEx) {
        return getMessage(appEx.getMsgCode(), appEx.getArgs());
    }

    public String getMessage(MessageSourceResolvable resolvable) {
        return messageSource.getMessage(resolvable, LocaleContextHolder.getLocale());
    }
}