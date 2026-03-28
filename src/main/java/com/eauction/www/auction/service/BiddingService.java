package com.eauction.www.auction.service;

import com.eauction.www.auction.constants.ErrorConstants;
import com.eauction.www.auction.dto.BidEntity;
import com.eauction.www.auction.exception.AuctionServiceException;
import com.eauction.www.auction.exception.handler.BidServiceException;
import com.eauction.www.auction.models.*;
import com.eauction.www.auction.repository.BidRepository;
import com.eauction.www.auction.security.RequestContext;
import com.eauction.www.auction.util.ConverterUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BiddingService {

    private final BidRepository bidRepository;
    private final RequestContext requestContext;
    private final AuctionService auctionService;

    /**
     * Core Bidding Logic:
     * 1. Validates Auction Status & User Permissions.
     * 2. Locks the highest bid record to prevent race conditions.
     * 3. Compares and saves the new bid.
     */
    @Transactional
    public BidResponse applyBid(RequestUserBid requestUserBid, Authentication authentication) {
        String username = authentication.getName();

        // 1. Pre-validation (Status, Ownership, Admin rights)
        // Done before the lock to keep the "critical section" short.
        Auction auction = preValidateUserAndAuction(requestUserBid, username);

        // 2. Fetch current highest bid WITH PESSIMISTIC LOCK
        // This ensures no other user can process a bid for this item simultaneously.
        Optional<BidEntity> highestBidEntity = bidRepository.findHighestBidByItemIdAndAuctionId(
                requestUserBid.getItemId(), requestUserBid.getAuctionId());

        Double currentHighest = highestBidEntity.map(BidEntity::getBid).orElse(0.0);

        // 3. Bid Value Validation
        if (requestUserBid.getBid() <= currentHighest) {
            throw new BidServiceException(
                    "Your bid must be higher than the current highest: " + currentHighest,
                    ServiceErrorCode.BID_IS_LESS_THAN_CURR_VALUE);
        }

        // 4. Save the new Bid record
        BidEntity bidEntity = new BidEntity();

        bidEntity.setAuctionId(requestUserBid.getAuctionId());
        bidEntity.setItemId(requestUserBid.getItemId());
        bidEntity.setUsername(username);
        bidEntity.setBid(requestUserBid.getBid().doubleValue());
        bidEntity.setBidValueAtThatTime(currentHighest);

        BidEntity savedBid = bidRepository.save(bidEntity);

        log.info("Bid successful: User {} placed {} on Item {}", username, savedBid.getBid(), requestUserBid.getItemId());

        return BidResponse.builder()
                .yourBid(savedBid.getBid())
                .currentBid(savedBid.getBid())
                .build();
    }

    /**
     * Performs static checks that don't change based on other bids.
     */
    private Auction preValidateUserAndAuction(RequestUserBid requestUserBid, String username) {
        // Is it an Admin?
        if (requestContext.isAdmin()) {
            throw new BidServiceException("Admins are not allowed to bid", ServiceErrorCode.ADMIN_CANNOT_BID);
        }

        // Fetch Auction details
        Auction auction = auctionService.getAuctionViaAuctionId(requestUserBid.getAuctionId());
        if (auction == null) {
            throw new BidServiceException("Auction not found", ServiceErrorCode.INVALID_AUCTION_ID);
        }

        // Is Auction Active?
        if (!AuctionStatus.IN_PROGRESS.equals(auction.getStatus())) {
            throw new BidServiceException(ErrorConstants.AUCTION_NOT_ACTIVE, ServiceErrorCode.AUCTION_NOT_ACTIVE);
        }

        if(auction.isTemporarilyStopped()) {
            throw new BidServiceException(ErrorConstants.AUCTION_TEMPORARILY_STOPPED, ServiceErrorCode.AUCTION_TEMPORARILY_STOPPED);
        }

        // Is the bidder the owner?
        if (auction.getUsername().equals(username)) {
            throw new AuctionServiceException(ErrorConstants.OWNER_BID_NOT_ALLOWED, ServiceErrorCode.BIDDER_SAME_AS_OWNER);
        }

        return auction;
    }

    public List<Bid> getBidsViaAuctionIdAndItemId(String auctionId, String itemId, Authentication authentication) {

        String username = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return ConverterUtility.convertToBidList(
                    bidRepository.findBidsByItemIdAndAuctionId(itemId, auctionId));

        } else {
            return ConverterUtility.convertToBidList(
                    bidRepository.findBidsByItemIdAndAuctionIdAndUsername(itemId, auctionId, username));
        }

    }

    public BidEntity getCurrentHighestBidder(String auctionId, String itemId) {
        return bidRepository.findHighestBidByItemIdAndAuctionId(itemId, auctionId)
                .orElse(null);
    }

    public Bid getLatestBidViaAuctionIdAndItemId(String auctionId, String itemId) {
        return bidRepository.findHighestBidByItemIdAndAuctionId(itemId, auctionId)
                .map(ConverterUtility::convertToBid)
                .orElse(null);
    }

    public void deleteBid(String auctionId, String itemId, String bidId) {
        Optional<BidEntity> bidEntityOpt = bidRepository.findById(bidId);

        if (bidEntityOpt.isEmpty()) {
            throw new BidServiceException("Bid not found", ServiceErrorCode.NO_BIDS_TO_DELETE);
        }

        BidEntity bidEntity = bidEntityOpt.get();

        if (!bidEntity.getAuctionId().equals(auctionId) || !bidEntity.getItemId().equals(itemId)) {
            throw new BidServiceException("Bid does not belong to the specified auction/item", ServiceErrorCode.INVALID_INPUT);
        }

        bidRepository.deleteById(bidId);
    }
}