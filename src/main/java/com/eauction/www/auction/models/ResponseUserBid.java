package com.eauction.www.auction.models;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@Data
public class ResponseUserBid {

    private Integer yourBid;
    private Integer currentBid;
    private List<Bid> userBids;
    private Bid latestBid;

    public ResponseUserBid() {
    }

    public ResponseUserBid(List<Bid> userBids) {
        this.userBids = userBids;
    }

    public ResponseUserBid(Bid latestBid) {
        this.latestBid = latestBid;
    }

}
