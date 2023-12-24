package com.eauction.www.auction.service;

import com.eauction.www.auction.constants.ErrorConstants;
import com.eauction.www.auction.dto.AuctionEntity;
import com.eauction.www.auction.exception.AuctionServiceException;
import com.eauction.www.auction.models.Auction;
import com.eauction.www.auction.models.AuctionStatus;
import com.eauction.www.auction.models.Item;
import com.eauction.www.auction.models.ServiceErrorCode;
import com.eauction.www.auction.repository.AuctionRepository;
import com.eauction.www.auction.security.RequestContext;
import com.eauction.www.auction.util.ConverterUtility;
import com.eauction.www.auction.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

import java.util.List;

@Service
public class AuctionService {

    @Autowired
    RequestContext requestContext;

    @Autowired
    AuctionRepository auctionRepository;

    public List<Auction> getAuctions() {
        return ConverterUtility.convertToAuctionList(auctionRepository.findAll());
    }

    public List<Auction> getAuctions(String username) {
        return ConverterUtility.convertToAuctionList(auctionRepository.findByUsername(username));
    }

    public List<Auction> getAuctions(long fromTimestamp, long toTimestamp) {
        return ConverterUtility.convertToAuctionList(
                auctionRepository.findAuctionsBetweenStartTimestamp(fromTimestamp,toTimestamp));

    }

    public Auction createAuction(Auction auction) {
        validateUser(auction, requestContext.isAdmin());// in case auction created by Admin on behalf of user.
        if(!validateTime(auction)) {
            throw new AuctionServiceException(
                    ErrorConstants.WRONG_AUCTION_TIMESTAMP,
                    ServiceErrorCode.WRONG_AUCTION_TIMESTAMP);
        }
        //Utility.populateCurrentTime(auction);
        Utility.populateStartStopTime(auction);
        auction.setCreatedBy(requestContext.getUsername());

        /**
         * if Auction is created by a User, set username to username of the user
         * else set it to null
         */
        if (!requestContext.isAdmin()) {
            System.out.println("");
            auction.setUsername(requestContext.getUsername());
        }
        return new Auction(auctionRepository.save(new AuctionEntity(auction)));

    }

    private boolean validateTime(Auction auction) {
        return auction.getStartTimestamp() > Instant.now().toEpochMilli()
                && auction.getStopTimestamp() > Instant.now().toEpochMilli()
                && auction.getStopTimestamp() > auction.getStartTimestamp();
    }

    private boolean validateUser(Auction auction, boolean isCreatedByAdmin) {
        return true;
        // TODO: validate the username if createdBy Admin
    }

    public Auction getAuctionViaAuctionId(String auctionId) {
        return auctionRepository.findByAuctionId(auctionId)
                .map(Auction::new).orElse(null);
    }

    public Auction getAuctionViaIdAndUsername(String auctionId,String username) {
       return auctionRepository.findByAuctionIdAndUsername(auctionId,username)
               .map(Auction::new).orElse(null);
    }

    public boolean isAuctionActive(String auctionId) {
        return AuctionStatus.IN_PROGRESS.equals(getAuctionViaAuctionId(auctionId).getStatus());
    }

    public boolean isAuctionFinished(String auctionId) {
        return AuctionStatus.FINISHED.equals(getAuctionViaAuctionId(auctionId).getStatus());
    }

    public List<Item> getItemsforAuction(String auctionId) {
        Auction auction = getAuctionViaAuctionId(auctionId);
        if (null != auction) {
            return auction.getItems();
        } else {
            throw new AuctionServiceException("Invalid AuctionId",ServiceErrorCode.INVALID_AUCTION_ID);
        }
    }
}
