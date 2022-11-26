package com.eauction.www.auction.models;

public enum ServiceErrorCode {

    // Owner of the auction is not allowed to bid in his own Auction.
    BIDDER_SAME_AS_OWNER("AUC001");

    private String value;

    private ServiceErrorCode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
