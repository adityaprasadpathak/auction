package com.eauction.www.auction.models;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

@Builder
@AllArgsConstructor
public class ResponseUserBid {

    private Integer yourBid;
    private Integer currentBid;
    List<Bid> userBids;

    public ResponseUserBid() {
    }

    public ResponseUserBid(List<Bid> userBids) {
        this.userBids = userBids;
    }

    public Integer getYourBid() {
        return yourBid;
    }

    public void setYourBid(Integer yourBid) {
        this.yourBid = yourBid;
    }

    public Integer getCurrentBid() {
        return currentBid;
    }

    public void setCurrentBid(Integer currentBid) {
        this.currentBid = currentBid;
    }

    public List<Bid> getUserBids() {
        return userBids;
    }

    public void setUserBids(List<Bid> userBids) {
        this.userBids = userBids;
    }
}
