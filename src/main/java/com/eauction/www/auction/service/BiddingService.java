package com.eauction.www.auction.service;

import com.eauction.www.auction.constants.ErrorConstants;
import com.eauction.www.auction.exception.AuctionServiceException;
import com.eauction.www.auction.exception.handler.BidServiceException;
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

    @Autowired AuctionService auctionService;

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

    public Integer getCurrentBid(String auctionId, String itemId) {
        return fakeDB.getCurrentBid(auctionId,itemId);
    }

    private boolean validateBid(RequestUserBid requestUserBid) {

        // Validate Auction is Active
        if(!validateAuction(requestUserBid.getAuctionId())) {
            throw new BidServiceException(ErrorConstants.AUCTION_NOT_ACTIVE, ServiceErrorCode.AUCTION_NOT_ACTIVE);
        }

        // bidder must not be the owner of the Auction
        validateBidder(requestUserBid.getAuctionId(), requestContext.getUsername());


        // TODO: an Admin can't bid.

        // TODO: validate current bid should be less than the bid applied by the user.
        if(!validateBidvalue(requestUserBid.getAuctionId(), requestUserBid.getItemId(), requestUserBid.getBid())) {
            throw new BidServiceException(ErrorConstants.BID_LESS_CURR_VAL, ServiceErrorCode.BID_IS_LESS_THAN_CURR_VALUE);
        }

        // TODO: later introduce minimum increment for a valid bid.
        // Minimum increment should be decided by auction-owner per auction.
        // But there has to be a minimum and maximum limit set by Admin

        return true;

    }

    private boolean validateAuction(String auctionId) {
        Auction auction = auctionService.getAuctionViaAuctionId(auctionId);
        if (null != auction) {
            return auction.getStatus().equals(AuctionStatus.IN_PROGRESS);
        }
        return false;
    }

    private boolean validateBidvalue(String auctionId, String itemId, Integer bidvalue) {
        return getCurrentBid(auctionId, itemId) < bidvalue;
    }

    private boolean validateBidder(String auctionId, String username) {

        /**
         * username : is bidder username
         * auctionId : uniqueId of Auction
         *
         * if an auction present with given username and auctionId, means given username is owner of Auction. hence,
         * owner can't bid in his own auction.
         */
        Auction auction = fakeDB.getAuctionViaIdAndUserId(auctionId, username);
        if (null != auction) {
            throw new AuctionServiceException(ErrorConstants.OWNER_BID_NOT_ALLOWED,
                    ServiceErrorCode.BIDDER_SAME_AS_OWNER);
        }
        return true;
    }

    public List<Bid> getUserBidsViaAuctionId(String auctionId, String username) {

        return fakeDB.getUserBidsViaAuctionId(auctionId, username);
    }
}
