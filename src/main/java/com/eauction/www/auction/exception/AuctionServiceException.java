package com.eauction.www.auction.exception;

import com.eauction.www.auction.models.ServiceErrorCode;

public class AuctionServiceException extends RuntimeException {

    private ServiceErrorCode serviceErrorCode;

    public AuctionServiceException(ServiceErrorCode serviceErrorCode) {
        this.serviceErrorCode = serviceErrorCode;
    }

    public AuctionServiceException(String message, ServiceErrorCode serviceErrorCode) {
        super(message);
        this.serviceErrorCode = serviceErrorCode;
    }

    public AuctionServiceException(String message, Throwable cause, ServiceErrorCode serviceErrorCode) {
        super(message, cause);
        this.serviceErrorCode = serviceErrorCode;
    }

    public AuctionServiceException(Throwable cause, ServiceErrorCode serviceErrorCode) {
        super(cause);
        this.serviceErrorCode = serviceErrorCode;
    }

    public AuctionServiceException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace, ServiceErrorCode serviceErrorCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.serviceErrorCode = serviceErrorCode;
    }

    public ServiceErrorCode getServiceErrorCode() {
        return serviceErrorCode;
    }
}
