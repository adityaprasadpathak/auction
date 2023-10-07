package com.eauction.www.auction.service;

import com.eauction.www.auction.constants.ErrorConstants;
import com.eauction.www.auction.exception.AuctionServiceException;
import com.eauction.www.auction.models.Auction;
import com.eauction.www.auction.models.ServiceErrorCode;
import com.eauction.www.auction.security.RequestContext;
import com.eauction.www.auction.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

import java.util.List;
import java.util.Optional;

@Service
public class AuctionService {

    @Autowired
    RequestContext requestContext;

    @Autowired
    FakeDB fakeDB;

    public List<Auction> getAuctions() {
        return fakeDB.getAuctions();
    }

    public List<Auction> getAuctions(String username) {
        return fakeDB.getAuctionViaUsername(username);
    }

    public List<Auction> getAuctions(long fromTimestamp, long toTimestamp) {
        return fakeDB.getAuctionBetweenTimestamp(fromTimestamp, toTimestamp);
    }

    public Auction getAuctions(String userName, String auctionId) {
        Optional<Auction> auctionOptional = getAuctions().stream()
                .filter(auction -> auction.getAuctionId().equals(auctionId)
                        && auction.getUsername().equals(userName))
                .findFirst();
        return auctionOptional.isPresent() ? auctionOptional.get() : null;
    }

    public Auction createAuction(Auction auction) {
        validateUser(auction, requestContext.isAdmin());// in case auction created by Admin on behalf of user.
        if(!validateTime(auction)) {
            throw new AuctionServiceException(
                    ErrorConstants.WRONG_AUCTION_TIMESTAMP,
                    ServiceErrorCode.WRONG_AUCTION_TIMESTAMP);
        }
        Utility.createAndPopulateAuctionId(auction);
        Utility.populateCurrentTime(auction);
        Utility.populateStartStopTime(auction);
        auction.setCreatedBy(requestContext.getUsername());
        if (!requestContext.isAdmin()) {
            System.out.println("");
            auction.setUsername(requestContext.getUsername());
        }
        Auction a = fakeDB.createAuction(auction);
        if (null != a) {
            return a;
        } else {
            throw new RuntimeException("Unable to create Exception");
        }

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
        return getAuctions().stream()
                .filter(auction -> auction.getAuctionId().equals(auctionId))
                .findFirst()
                .orElse(null);
    }
}
