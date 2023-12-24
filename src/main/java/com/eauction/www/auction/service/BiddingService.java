package com.eauction.www.auction.service;

import com.eauction.www.auction.constants.ErrorConstants;
import com.eauction.www.auction.dto.BidEntity;
import com.eauction.www.auction.exception.AuctionServiceException;
import com.eauction.www.auction.exception.handler.BidServiceException;
import com.eauction.www.auction.models.*;
import com.eauction.www.auction.repository.BidRepository;
import com.eauction.www.auction.security.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BiddingService {

    @Autowired
    FakeDB fakeDB;

    @Autowired
    BidRepository bidRepository;

    @Autowired
    RequestContext requestContext;

    @Autowired
    AuctionService auctionService;

    public synchronized ResponseUserBid applyBid(RequestUserBid requestUserBid, String username) {
        validateBid(requestUserBid);
        BidEntity bidEntity = new BidEntity();
        bidEntity.setAuctionId(requestUserBid.getAuctionId());
        bidEntity.setItemId(requestUserBid.getItemId());
        bidEntity.setUsername(requestContext.getUsername());
        bidEntity.setBid(requestUserBid.getBid());
        bidEntity.setBidValueAtThatTime(getCurrentHighestBid(requestUserBid.getAuctionId(), requestUserBid.getItemId()));

        BidEntity responseBidEntity = bidRepository.save(bidEntity);//fakeDB.addBid(bid);

        return ResponseUserBid.builder()
                .yourBid(responseBidEntity.getBid())
                .currentBid(getCurrentHighestBid(requestUserBid.getAuctionId(), requestUserBid.getItemId()))
                .build();
    }

    /**
     * Retrieves the current highest bid for a specific item and auction.
     *
     * @param auctionId The unique identifier of the auction.
     * @param itemId    The unique identifier of the item.
     * @return The current highest bid value, or {@code null} if there are no bids.
     */
    public Integer getCurrentHighestBid(String auctionId, String itemId) {
        //return fakeDB.getCurrentBid(auctionId,itemId);
        Optional<BidEntity> highestBidSoFar = bidRepository.findHighestBidByItemIdAndAuctionId(itemId, auctionId);
        return highestBidSoFar.map(BidEntity::getBid).orElse(null);
    }


    public BidEntity getCurrentHighestBidder(String auctionId, String itemId) {
        //return fakeDB.getCurrentBid(auctionId,itemId);
        Optional<BidEntity> highestBidSoFar = bidRepository.findHighestBidByItemIdAndAuctionId(itemId, auctionId);
        return highestBidSoFar.orElse(null);
    }

    private boolean validateBid(RequestUserBid requestUserBid) {

        // Validate Auction is Active
        if (!validateAuction(requestUserBid.getAuctionId())) {
            throw new BidServiceException(ErrorConstants.AUCTION_NOT_ACTIVE, ServiceErrorCode.AUCTION_NOT_ACTIVE);
        }

        // bidder must not be the owner of the Auction
        validateBidder(requestUserBid.getAuctionId(), requestContext.getUsername());


        // TODO: an Admin can't bid.

        // TODO: validate current bid should be less than the bid applied by the user.
        if (!validateBidValue(requestUserBid.getAuctionId(), requestUserBid.getItemId(), requestUserBid.getBid())) {
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

    private boolean validateBidValue(String auctionId, String itemId, Integer bidValue) {
        Integer currBid = getCurrentHighestBid(auctionId, itemId);
        return currBid == null || currBid < bidValue;
    }

    private boolean validateBidder(String auctionId, String username) {

        /**
         * username : is bidder username
         * auctionId : uniqueId of Auction
         *
         * if an auction present with given username and auctionId, means given username is owner of Auction. hence,
         * owner can't bid in his own auction.
         */

        Auction auction = auctionService.getAuctionViaIdAndUsername(auctionId, username);//fakeDB.getAuctionViaIdAndUserId(auctionId, username);
        if (null != auction) {
            throw new AuctionServiceException(ErrorConstants.OWNER_BID_NOT_ALLOWED, ServiceErrorCode.BIDDER_SAME_AS_OWNER);
        }
        return true;
    }

    public List<Bid> getUserBidsViaAuctionId(String auctionId, String username) {

        return fakeDB.getUserBidsViaAuctionId(auctionId, username);
    }
}
