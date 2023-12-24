package com.eauction.www.auction.models;

import com.eauction.www.auction.dto.BidEntity;

public class Bid {

    public Bid(BidEntity bidEntity) {
        this.auctionId = bidEntity.getAuctionId();
        this.itemId = bidEntity.getItemId();
        this.bidTime = bidEntity.getBidTime();
        this.bidValueAtThatTime = bidEntity.getBidValueAtThatTime();
        this.username = bidEntity.getUsername();

    }

    private String bidId;
    private String auctionId;
    private String itemId;
    private Integer bid;
    private Integer bidValueAtThatTime;
    private String username;
    private Long bidTime;

    // remove this Constructor later
    // BidId will be created by DB itself
    public Bid(String bidId) {
        this.bidId = bidId;
    }

    public String getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(String auctionId) {
        this.auctionId = auctionId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Integer getBid() {
        return bid;
    }

    public void setBid(Integer bid) {
        this.bid = bid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getBidTime() {
        return bidTime;
    }

    public void setBidTime(Long bidTime) {
        this.bidTime = bidTime;
    }

    public String getBidId() {
        return bidId;
    }

    public void setBidId(String bidId) {
        this.bidId = bidId;
    }

    public Integer getBidValueAtThatTime() {
        return bidValueAtThatTime;
    }

    public void setBidValueAtThatTime(Integer bidValueAtThatTime) {
        this.bidValueAtThatTime = bidValueAtThatTime;
    }
}
