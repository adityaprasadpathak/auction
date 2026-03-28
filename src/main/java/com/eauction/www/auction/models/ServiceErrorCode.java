package com.eauction.www.auction.models;

public enum ServiceErrorCode {

    // Owner of the auction is not allowed to bid in his own Auction.
    BIDDER_SAME_AS_OWNER("BID001"),
    BID_IS_LESS_THAN_CURR_VALUE("BID002"),
    AUCTION_NOT_ACTIVE("BID003"),
    WRONG_AUCTION_TIMESTAMP("AUC001"),
    AUCTION_NOT_FINISHED("AUC002"),
    INVALID_AUCTION_ID("AUC003"),
    ITEM_DOESNT_BELONG_TO_AUCTION("AUC004"),
    AUCTION_NOT_UPCOMING("AUC005"),
    AUCTION_NOT_AWAITING_APPROVAL("AUC006"),

    USER_NOT_FOUND("USR001"),
    INVALID_INPUT("REQ001"),

    ADMIN_CANNOT_BID("BID004"),
    NO_BIDS_TO_DELETE("BID005"),

    RESULT_NOT_DECLARED("RES001"),
    AUCTION_TEMPORARILY_STOPPED("RES002");

    private String value;

    private ServiceErrorCode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
