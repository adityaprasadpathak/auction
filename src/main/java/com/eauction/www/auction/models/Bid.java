package com.eauction.www.auction.models;

import com.eauction.www.auction.dto.BidEntity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Bid {

    public Bid(BidEntity bidEntity) {
        this.auctionId = bidEntity.getAuctionId();
        this.itemId = bidEntity.getItemId();
        this.bidTime = bidEntity.getBidTime();
        this.bidValueAtThatTime = bidEntity.getBidValueAtThatTime();
        this.username = bidEntity.getUsername();
        this.bid = bidEntity.getBid();
        this.bidId = bidEntity.getId();

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

}
