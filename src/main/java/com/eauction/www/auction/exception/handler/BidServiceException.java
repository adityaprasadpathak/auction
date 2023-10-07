package com.eauction.www.auction.exception.handler;

import com.eauction.www.auction.models.ServiceErrorCode;

public class BidServiceException extends RuntimeException{

    private ServiceErrorCode serviceErrorCode;

    public BidServiceException(ServiceErrorCode serviceErrorCode) {
        this.serviceErrorCode = serviceErrorCode;
    }

    public BidServiceException(String message, ServiceErrorCode serviceErrorCode) {
        super(message);
        this.serviceErrorCode = serviceErrorCode;
    }

    public BidServiceException(String message, Throwable cause, ServiceErrorCode serviceErrorCode) {
        super(message, cause);
        this.serviceErrorCode = serviceErrorCode;
    }

    public BidServiceException(Throwable cause, ServiceErrorCode serviceErrorCode) {
        super(cause);
        this.serviceErrorCode = serviceErrorCode;
    }

    public BidServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ServiceErrorCode serviceErrorCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.serviceErrorCode = serviceErrorCode;
    }
}
