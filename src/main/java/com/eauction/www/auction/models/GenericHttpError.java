package com.eauction.www.auction.models;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class GenericHttpError {

    private Integer errorCode;
    private String errorStatus;
    private String errorMessage;
    private String displayMessage;
    private String serviceErrorCode;
    private String exceptionStacktrace;
}
