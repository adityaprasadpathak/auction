package com.eauction.www.auction.error.models;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Builder
@Data
public class GenericHttpError {

    private String errorStatus;
    private int errorCode;
    private String errorMessage;
    private String displayMessage;
    private String serviceErrorCode; // Optional for business codes
    private String exceptionStacktrace;
    private String path;
    private long timestamp;
    private Map<String, String> validationErrors; // Added for @Valid support
}
