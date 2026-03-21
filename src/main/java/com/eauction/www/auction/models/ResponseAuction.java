package com.eauction.www.auction.models;

import java.util.List;

/**
 * ResponseAction has two fields,
 * one is List of Auction and
 * another is single Auction.
 * This class will be used as response for all Api's related to Auction.
 *
 * When AuctionId is passed as a filter in the Api, then only single Auction field will be populated and List of Auction will be null.
 * and
 * when AuctionId is not passed as a filter in the Api, then List of Auction (created by calling user) field will be populated and single Auction field will be null.
 */
public class ResponseAuction {

    List<Auction> auctions;
    Auction auction;

    public ResponseAuction(List<Auction> auctions) {
        this.auctions = auctions;
    }

    public ResponseAuction(Auction auction) {
        this.auction = auction;
    }

    public List<Auction> getAuctions() {
        return auctions;
    }

    public void setAuctions(List<Auction> auctions) {
        this.auctions = auctions;
    }

    public Auction getAuction() {
        return auction;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }
}
