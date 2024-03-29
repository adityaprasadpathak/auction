package com.eauction.www.auction.service;

import com.eauction.www.auction.models.Auction;
import com.eauction.www.auction.models.Bid;
import com.eauction.www.auction.models.ResponseUserBid;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FakeDB {

    List<Auction> auctions = new ArrayList<>();
    List<Bid> bids = new ArrayList<>();

    public Auction createAuction(Auction auction) {

        boolean add = auctions.add(auction);
        return (add) ? auction : null;
    }

    public List<Auction> getAuctions() {

        return auctions;
    }

    public Auction getAuctionViaId(String auctionId) {
        Optional<Auction> optAuction = getAuctions().stream()
                .filter(auction -> auction.getAuctionId().equals(auctionId)).findFirst();
        return optAuction.isPresent() ? optAuction.get() : null;
    }

    public List<Auction> getAuctionViaUsername(String username) {
        return getAuctions().stream().filter(auction -> auction.getUsername().equals(username))
                .collect(Collectors.toList());
    }

    public Auction getAuctionViaIdAndUserId(String auctionId, String userId) {
        return getAuctions().stream()
                .filter(auction -> (auction.getUsername().equals(userId) && auction.getAuctionId().equals(auctionId)))
                .findFirst().orElse(null);

    }

    public ResponseUserBid addBid(Bid bid) {
        bid.setBidValueAtThatTime(getCurrentBid(bid.getAuctionId(), bid.getItemId()));
        bids.add(bid);

        ResponseUserBid responseUserBid = new ResponseUserBid();
        responseUserBid.setYourBid(bid.getBid());
        responseUserBid.setCurrentBid(getCurrentBid(bid.getAuctionId(), bid.getItemId()));

        return responseUserBid;

    }

    /**
     * This method will return all the bids made by a user in an auction, including all items
     * @param auctionId
     * @param username
     * @return
     */
    public List<Bid> getUserBidsViaAuctionId(String auctionId, String username) {

        return bids.stream()
                .filter(bid -> bid.getAuctionId().equals(auctionId) && bid.getUsername().equals(username))
                .collect(Collectors.toList());
    }

    public List<Auction> getAuctionBetweenTimestamp(long fromTimestamp, long toTimestamp) {
       return getAuctions().parallelStream().filter(auction -> auction.getStartTimestamp() >= fromTimestamp
                || auction.getStartTimestamp() <= toTimestamp).collect(Collectors.toList());
    }

    public Integer getCurrentBid(String auctionId, String itemId) {
        Optional<Integer> highestBid = bids.parallelStream().
                filter(bid -> bid.getAuctionId().equals(auctionId)
                && bid.getItemId().equals(itemId))
                .map(Bid::getBid)
                .max(Comparator.naturalOrder());
        return highestBid.orElse(0);
    }
}
