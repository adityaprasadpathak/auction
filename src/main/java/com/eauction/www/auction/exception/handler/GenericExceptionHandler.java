package com.eauction.www.auction.exception.handler;

import com.eauction.www.auction.exception.AuctionServiceException;
import com.eauction.www.auction.models.GenericHttpError;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GenericExceptionHandler {

    @ExceptionHandler(AuctionServiceException.class)
    public ResponseEntity<GenericHttpError> handleAuctionServiceException(AuctionServiceException ase){

        GenericHttpError genericHttpError = GenericHttpError.builder()
                .errorStatus(HttpStatus.PRECONDITION_FAILED.toString())
                .errorCode(HttpStatus.PRECONDITION_FAILED.value())
                .errorMessage(ase.getMessage())
                .displayMessage(ase.getMessage())
                .serviceErrorCode(ase.getServiceErrorCode().getValue())
                .exceptionStacktrace(ExceptionUtils.getStackTrace(ase))
                .build();

        return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(genericHttpError);
    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<GenericHttpError> handleBadCredentialsException(BadCredentialsException bce){

        GenericHttpError genericHttpError = GenericHttpError.builder()
                .errorStatus(HttpStatus.UNAUTHORIZED.toString())
                .errorCode(HttpStatus.UNAUTHORIZED.value())
                .errorMessage("Wrong username/password has provided")
                .displayMessage("Bad Credentials.PLease try Again")
                .exceptionStacktrace(ExceptionUtils.getStackTrace(bce)).build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(genericHttpError);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<GenericHttpError> handleLockedException(LockedException le){

        GenericHttpError genericHttpError = GenericHttpError.builder()
                .errorStatus(HttpStatus.UNAUTHORIZED.toString())
                .errorCode(HttpStatus.UNAUTHORIZED.value())
                .errorMessage("User is locked. Please contact administrator.")
                .displayMessage("User is locked. Please contact administrator.")
                .exceptionStacktrace(ExceptionUtils.getStackTrace(le)).build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(genericHttpError);
    }


}
