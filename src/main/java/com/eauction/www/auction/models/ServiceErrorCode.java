package com.eauction.www.auction.models;

public enum ServiceErrorCode {

    // Owner of the auction is not allowed to bid in his own Auction.
    BIDDER_SAME_AS_OWNER("BID001"),
    BID_IS_LESS_THAN_CURR_VALUE("BID002"),
    AUCTION_NOT_ACTIVE("BID003"),
    WRONG_AUCTION_TIMESTAMP("AUC001");

    private String value;

    private ServiceErrorCode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
