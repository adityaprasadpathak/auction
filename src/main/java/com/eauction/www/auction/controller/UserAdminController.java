package com.eauction.www.auction.controller;

import com.eauction.www.auction.models.*;
import com.eauction.www.auction.service.AuctionService;
import com.eauction.www.auction.service.BiddingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * As this is not open controller, hence all Api's here in this controller needs AuthToken(JWT token) to make a call.
 * UserAdminController as name suggest, will contain common Api's which needs to be called by both user and Admin.
 */
@CrossOrigin
@RestController
@RequestMapping("/useradmin")
@RequiredArgsConstructor
@Slf4j
public class UserAdminController {

    @Autowired
    public AuctionService auctionService;

    private final BiddingService biddingService;

    /**
     * This Api is used to create auction. Auction will always have an owner, which will be the user who created it.
     *
     * An Admin can also create an auction on behalf of a user. Remember an Admin can never be an owner of the auction.
     *
     * An Admin can never bid in an Auction
     *
     * @param auction
     *
     * @return
     */
    @PostMapping(value = "/auction")
    public Auction createAuction(@RequestBody Auction auction) {
        return auctionService.createAuction(auction);
    }

    @GetMapping("/auctions")
    public ResponseEntity<ResponseAuction> getAuctions(
            @RequestParam(required = false) AuctionStatus auctionStatus,
            Authentication authentication) {

        String username = authentication.getName();
        ResponseAuction responseAuction;
        if (auctionStatus != null) {
            responseAuction  = new ResponseAuction(auctionService.getAuctionsViaAuctionStatus(auctionStatus));
        } else {
            responseAuction = new ResponseAuction(auctionService.getAuctions());
        }


        return ResponseEntity.ok(responseAuction);
    }

    @GetMapping("/auctions/{auctionId}")
    public ResponseEntity<ResponseAuction> getAuctions(
            @PathVariable String auctionId,
            Authentication authentication) {

        String username = authentication.getName();

        ResponseAuction responseAuction = new ResponseAuction(auctionService.getAuctionViaAuctionId(auctionId));

        return ResponseEntity.ok(responseAuction);
    }

    @GetMapping("/bid/auctions/{auctionId}/items/{itemId}")
    public ResponseEntity<ResponseUserBid> getUserBids(
            @PathVariable String auctionId,
            @PathVariable String itemId,
            @RequestParam(defaultValue = "false") boolean latestBidOnly,
            Authentication authentication) {

        log.info("Received request to get user bids for auctionId: {}, itemId: {}, latestBidOnly: {}", auctionId, itemId, latestBidOnly);

        if (latestBidOnly) {
            log.info("Fetching latest bid for auctionId: {}, itemId: {}", auctionId, itemId);
            return ResponseEntity.ok(new ResponseUserBid(biddingService.getLatestBidsViaAuctionIdAndItemId(auctionId, itemId)));
        }

        log.info("Fetching all bids for auctionId: {}, itemId: {}", auctionId, itemId);
        return ResponseEntity.ok(new ResponseUserBid(biddingService.getUserBidsViaAuctionIdAndItemId(auctionId, itemId)));
    }



}
