package com.eauction.www.auction.task;

import com.eauction.www.auction.models.Auction;
import com.eauction.www.auction.models.AuctionStatus;
import com.eauction.www.auction.service.AuctionService;

public class StopAuction implements Runnable {

    private Auction auction;
    private AuctionService auctionService;

    public StopAuction(Auction auction, AuctionService auctionService) {
        this.auction = auction;
        this.auctionService = auctionService;
    }
    @Override
    public void run() {
        System.out.println("StopAuction::"+auction.getAuctionId());
        auctionService.getAuctions()
                .stream()
                .filter(a-> auction.getAuctionId().equals(a.getAuctionId()))
                .findFirst()
                .ifPresent(a->a.setStatus(AuctionStatus.FINISHED));
    }
}
