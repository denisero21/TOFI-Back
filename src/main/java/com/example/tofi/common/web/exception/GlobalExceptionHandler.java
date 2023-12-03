package com.example.tofi.common.web.exception;

import com.example.tofi.common.exception.DataNotFoundException;
import com.example.tofi.common.exception.JwtTokenException;
import com.example.tofi.common.exception.OtpException;
import com.example.tofi.common.exception.exception.CommissionNotFoundException;
import com.example.tofi.common.exception.exception.SavedCardNotFoundException;
import com.example.tofi.common.exception.exception.TwoFactorAuthException;
import com.example.tofi.common.util.MessageUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

import static com.example.tofi.common.web.exception.ErrorType.*;


@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    private final MessageUtil ms;
    private static final String EXCEPTION_DUPLICATE_EMAIL = "error.user.duplicateEmail";
    private static final String EXCEPTION_DUPLICATE_ROLE = "error.user.duplicateRole";
    private static final String EXCEPTION_DUPLICATE_BANK_COMMISSION_PAIR = "error.bankCommission.duplicatePairs";
    private static final String EXCEPTION_DUPLICATE_PAYER_COMMISSION_PAIR = "error.payerCommission.duplicatePairs";
    private static final String EXCEPTION_DUPLICATE_VP_COMMISSION_PAIR = "error.virtualPosCommission.duplicatePairs";
    private static final String EXCEPTION_DUPLICATE_VP_TERMINALS_PAIR = "error.virtualPosTerminals.duplicatePairs";

    private static final Map<String, String> CONSTRAINS_I18N_MAP =
            Map.of(
                    "users_unique_email_idx", EXCEPTION_DUPLICATE_EMAIL,
                    "users_role_idx", EXCEPTION_DUPLICATE_ROLE,
                    "bank_commission_pairs_unique", EXCEPTION_DUPLICATE_BANK_COMMISSION_PAIR,
                    "virtual_pos_terminals_unique", EXCEPTION_DUPLICATE_VP_TERMINALS_PAIR,
                    "payer_commission_pairs_unique", EXCEPTION_DUPLICATE_PAYER_COMMISSION_PAIR,
                    "virtual_pos_commission_pairs_unique", EXCEPTION_DUPLICATE_VP_COMMISSION_PAIR);

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorInfo applicationError(HttpServletRequest req, HttpServletResponse resp, Exception e) {
        return logAndGetErrorInfo(req, resp, e, APP_ERROR);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            BadCredentialsException.class,
            TwoFactorAuthException.class,
            OtpException.class})
    public ErrorInfo error(HttpServletRequest req, HttpServletResponse resp, Exception e) {
        return logAndGetErrorInfo(req, resp, e, BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
            InternalAuthenticationServiceException.class,
            DataNotFoundException.class,
            CommissionNotFoundException.class,
            SavedCardNotFoundException.class,
            EntityNotFoundException.class})
    public ErrorInfo error404(HttpServletRequest req, HttpServletResponse resp, Exception e) {
        return logAndGetErrorInfo(req, resp, e, DATA_NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(JwtTokenException.class)
    public ErrorInfo unauthorized(HttpServletRequest req, HttpServletResponse resp, Exception e) {
        return logAndGetErrorInfo(req, resp, e, UNAUTHORIZED);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorInfo conflict(HttpServletRequest req, HttpServletResponse resp, ContentCachingResponseWrapper res,
                              DataIntegrityViolationException e) {
        var rootMsg = getRootCause(e).getMessage();
        if (rootMsg != null) {
            var lowerCaseMsg = rootMsg.toLowerCase();
            Optional<Map.Entry<String, String>> entry = CONSTRAINS_I18N_MAP.entrySet().stream()
                    .filter(it -> lowerCaseMsg.contains(it.getKey()))
                    .findAny();
            if (entry.isPresent()) {
                res.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
                return logAndGetErrorInfo(req, resp, e, VALIDATION_ERROR, ms.getMessage(entry.get().getValue()));
            }
        }
        res.setStatus(HttpStatus.CONFLICT.value());
        return logAndGetErrorInfo(req, resp, e, DATA_ERROR);
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorInfo> applicationError(HttpServletRequest req, HttpServletResponse resp,
                                                      ApplicationException appEx) {
        var errorInfo = logAndGetErrorInfo(req, resp, appEx, appEx.getType(), ms.getMessage(appEx));
        return ResponseEntity.status(appEx.getType().getStatus()).body(errorInfo);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ErrorInfo handleAccessDeniedException(HttpServletRequest req, HttpServletResponse resp,
                                                 AccessDeniedException e) {
        return logAndGetErrorInfo(req, resp, e, FORBIDDEN, "Access denied");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DisabledException.class)
    public ErrorInfo handleUserDisabledException(HttpServletRequest req, HttpServletResponse resp,
                                                 DisabledException e) {
        return logAndGetErrorInfo(req, resp, e, BAD_REQUEST, ms.getMessage("error.user.disabled"));
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    public ErrorInfo bindValidationError(HttpServletRequest req, HttpServletResponse resp, Exception e) {
        @SuppressWarnings("DataFlowIssue")
        var result = e instanceof BindException ?
                ((BindException) e).getBindingResult() : ((MethodArgumentNotValidException) e).getBindingResult();

        String[] details = result.getFieldErrors().stream()
                .map(ms::getMessage)
                .toArray(String[]::new);

        return logAndGetErrorInfo(req, resp, e, VALIDATION_ERROR, details);
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({IllegalRequestDataException.class,
            MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    public ErrorInfo illegalRequestDataError(HttpServletRequest req, HttpServletResponse resp, Exception e) {
        return logAndGetErrorInfo(req, resp, e, VALIDATION_ERROR);
    }

    private ErrorInfo logAndGetErrorInfo(HttpServletRequest req, HttpServletResponse resp, Exception e,
                                         ErrorType errorType, String... details) {
        // TODO: 30.07.2023 set in all modules parameters spring.http.encoding.charset=UTF-8 spring.http.encoding.enabled=true spring.http.encoding.force=true
        resp.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE + "; charset=" + StandardCharsets.UTF_8);
        e.printStackTrace();
        Throwable rootCause = getRootCause(e);
        String error = ms.getMessage(errorType.getErrorCode());
        String[] errorDescription = details.length != 0 ? details : new String[]{getMessage(rootCause)};

        ErrorInfo errorInfo = new ErrorInfo(
                req.getMethod(),
                req.getRequestURL(),
                req.getQueryString(),
                req.getRemoteAddr(),
                req.getUserPrincipal() != null ? req.getUserPrincipal().getName() : null,
                errorType,
                error,
                errorDescription);
        log.error(errorInfo.toString());

        errorInfo = new ErrorInfo();
        errorInfo.setError(error);
        errorInfo.setError_description(errorDescription);
        return errorInfo;
    }

    private Throwable getRootCause(Throwable t) {
        Throwable result = t;
        Throwable cause;

        while (null != (cause = result.getCause()) && (result != cause)) {
            result = cause;
        }
        return result;
    }

    private String getMessage(Throwable e) {
        return e.getLocalizedMessage() != null ? e.getLocalizedMessage() : e.getClass().getName();
    }

}
