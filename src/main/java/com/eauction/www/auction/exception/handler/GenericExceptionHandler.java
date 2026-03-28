package com.eauction.www.auction.exception.handler;

import com.eauction.www.auction.exception.AuctionServiceException;
import com.eauction.www.auction.error.models.GenericHttpError;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GenericExceptionHandler {

    // 1. Handle Bean Validation Errors (@Valid, @NotBlank, etc.)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GenericHttpError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage())
        );

        GenericHttpError error = GenericHttpError.builder()
                .errorStatus(HttpStatus.BAD_REQUEST.toString())
                .errorCode(HttpStatus.BAD_REQUEST.value())
                .displayMessage("Please correct the highlighted fields.")
                .errorMessage("Input validation failed")
                .validationErrors(fieldErrors)
                .path(request.getRequestURI())
                .timestamp(System.currentTimeMillis())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // 2. Handle Custom Business Exceptions
    @ExceptionHandler(AuctionServiceException.class)
    public ResponseEntity<GenericHttpError> handleAuctionServiceException(AuctionServiceException ase, HttpServletRequest request) {
        GenericHttpError error = GenericHttpError.builder()
                .errorStatus(HttpStatus.PRECONDITION_FAILED.toString())
                .errorCode(HttpStatus.PRECONDITION_FAILED.value())
                .errorMessage(ase.getMessage())
                .displayMessage(ase.getMessage())
                .serviceErrorCode(ase.getServiceErrorCode() != null ? ase.getServiceErrorCode().getValue() : null)
                .path(request.getRequestURI())
                .timestamp(System.currentTimeMillis())
                .exceptionStacktrace(ExceptionUtils.getStackTrace(ase))
                .build();

        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(error);
    }

    // 3. Handle Security: Bad Credentials
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<GenericHttpError> handleBadCredentialsException(BadCredentialsException bce, HttpServletRequest request) {
        GenericHttpError error = GenericHttpError.builder()
                .errorStatus(HttpStatus.UNAUTHORIZED.toString())
                .errorCode(HttpStatus.UNAUTHORIZED.value())
                .errorMessage("Invalid username or password provided")
                .displayMessage("Bad Credentials. Please try again.")
                .path(request.getRequestURI())
                .timestamp(System.currentTimeMillis())
                .exceptionStacktrace(ExceptionUtils.getStackTrace(bce))
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    // 4. Handle Security: Locked Account
    @ExceptionHandler(LockedException.class)
    public ResponseEntity<GenericHttpError> handleLockedException(LockedException le, HttpServletRequest request) {
        GenericHttpError error = GenericHttpError.builder()
                .errorStatus(HttpStatus.UNAUTHORIZED.toString())
                .errorCode(HttpStatus.UNAUTHORIZED.value())
                .errorMessage("User is locked.")
                .displayMessage("User is locked. Please contact administrator.")
                .path(request.getRequestURI())
                .timestamp(System.currentTimeMillis())
                .exceptionStacktrace(ExceptionUtils.getStackTrace(le))
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    // 5. Global Catch-All for Runtime/Internal Errors
    @ExceptionHandler({RuntimeException.class, Exception.class})
    public ResponseEntity<GenericHttpError> handleGlobalException(Exception ex, HttpServletRequest request) {
        log.error("Unhandled exception occurred at {}: ", request.getRequestURI(), ex);

        GenericHttpError error = GenericHttpError.builder()
                .errorStatus(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errorMessage(ex.getMessage())
                .displayMessage("An unexpected system error occurred.")
                .path(request.getRequestURI())
                .timestamp(System.currentTimeMillis())
                .exceptionStacktrace(ExceptionUtils.getStackTrace(ex))
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}