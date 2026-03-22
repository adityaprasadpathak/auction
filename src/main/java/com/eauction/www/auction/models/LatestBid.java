package com.eauction.www.auction.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LatestBid {

    private String auctionId;
    private String itemId;
    private Integer bid;
}
