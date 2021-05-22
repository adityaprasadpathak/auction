package com.eauction.www.auction.models;

public enum ServiceErrorCode {

    OWNER_SAME_AS_BIDDER("AUC001");

    private String value;
    private ServiceErrorCode(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
