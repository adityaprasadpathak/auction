package com.eauction.www.auction.exception;

import com.eauction.www.auction.models.GenericHttpError;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GenericExceptionHandler {


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<GenericHttpError> handlerBadCredentialsException(BadCredentialsException bce){

        GenericHttpError genericHttpError = GenericHttpError.builder()
                .errorStatus(HttpStatus.UNAUTHORIZED.toString())
                .errorCode(HttpStatus.UNAUTHORIZED.value())
                .errorMessage("Wrong username/password has provided")
                .displayMessage("Bad Credentials.PLease try Again")
                .exceptionStacktrace(ExceptionUtils.getStackTrace(bce)).build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(genericHttpError);
    }
}
