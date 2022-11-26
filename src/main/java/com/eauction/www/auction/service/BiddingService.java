package com.eauction.www.auction.service;

import com.eauction.www.auction.exception.AuctionServiceException;
import com.eauction.www.auction.models.*;
import com.eauction.www.auction.security.RequestContext;
import com.eauction.www.auction.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BiddingService {

    @Autowired
    FakeDB fakeDB;

    @Autowired
    RequestContext requestContext;

    public synchronized ResponseUserBid applyBid(RequestUserBid requestUserBid, String username) {
        validateBid(requestUserBid);
        Bid bid = new Bid(Utility.generateUniqueBidId());
        bid.setBid(requestUserBid.getBid());
        bid.setAuctionId(requestUserBid.getAuctionId());
        bid.setItemId(requestUserBid.getItemId());
        bid.setBidTime(System.currentTimeMillis());
        bid.setUsername(username);

        return fakeDB.addBid(bid);
    }

    private boolean validateBid(RequestUserBid requestUserBid) {

        // TODO: bidder must not be the owner of the Auction
        validateBidder(requestUserBid.getAuctionId(), requestContext.getUsername());
        return true;

        // TODO: an Admin cant bid.

        // TODO: validate current bid should be less than the bid applied by the user.

        // TODO: later introduce minimum increment for a valid bid.
        // Minimum increment should be decided by auction-owner per auction.
        // But there has to be a minimum and maximum limit set by Admin

    }

    private boolean validateBidder(String auctionId, String username) {

        /**
         * username : is bidder username auctionId : uniqueId of Auction
         *
         * if an auction present with given username and auctionId, means given username is owner of Auction. hence
         * owner can't bid in his own auction.
         */
        Auction auction = fakeDB.getAuctionViaIdAndUserId(auctionId, username);
        if (null != auction) {
            throw new AuctionServiceException("Owner is not allowed to bid in his own Auction",
                    ServiceErrorCode.BIDDER_SAME_AS_OWNER);
        }
        return true;
    }

    public List<Bid> getBids(String auctionId, String username) {

        return fakeDB.getBids(auctionId, username);
    }
}
