package com.eauction.www.auction.models;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@Data
public class BidResponse {

    private Double yourBid;
    private Double currentBid;
    private List<Bid> bids;
    private Bid latestBid;

    public BidResponse() {
    }

    public BidResponse(List<Bid> bids) {
        this.bids = bids;
    }

    public BidResponse(Bid latestBid) {
        this.latestBid = latestBid;
    }

}
