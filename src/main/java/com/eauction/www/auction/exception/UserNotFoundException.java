package com.eauction.www.auction.exception;

import com.eauction.www.auction.models.ServiceErrorCode;

public class UserNotFoundException extends RuntimeException {

    private ServiceErrorCode serviceErrorCode;

        public UserNotFoundException(String message, ServiceErrorCode serviceErrorCode) {
            super(message);
            this.serviceErrorCode = serviceErrorCode;
        }

    public UserNotFoundException() {
        super();
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotFoundException(Throwable cause) {
        super(cause);
    }

    protected UserNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
