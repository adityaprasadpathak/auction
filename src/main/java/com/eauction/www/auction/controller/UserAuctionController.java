package com.eauction.www.auction.controller;

import com.eauction.www.auction.models.RequestUserBid;
import com.eauction.www.auction.models.ResponseAuction;
import com.eauction.www.auction.models.ResponseUserBid;
import com.eauction.www.auction.security.RequestContext;
import com.eauction.www.auction.service.AuctionService;
import com.eauction.www.auction.service.BiddingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * As this is not open controller, hence all Api's here in this controller needs AuthToken(JWT token) to make a call.
 * UserAuctionController as name suggest will contains Api's which needs to be called by user only.
 */
@RestController
@RequestMapping("/users")
public class UserAuctionController {

    @Autowired
    public AuctionService auctionService;

    @Autowired
    public RequestContext requestContext;

    @Autowired
    BiddingService biddingService;

    /**
     * This Api will return List of Auctions created by the calling user, DESC sorted via auction start time.
     *
     * @param auctionId
     *            : if auctionId is provided as a filter, then only one auction matching with that id will be returned.
     *
     * @return ResponseAuction contains two properties 1. Auction 2. List<Auction> Auction will be populated if
     *         AuctionId is provided and List<Auction> will be null. if auctionId is not provided, List<Auction>
     *         properties will be populated, and Auction will be null.
     */
    @GetMapping(value = "/auctions")
    public ResponseEntity<ResponseAuction> getAuctions(@RequestParam(required = false) String auctionId) {

        ResponseAuction responseAuction = auctionId != null
                ? new ResponseAuction(auctionService.getAuctions(requestContext.getUsername(), auctionId))
                : new ResponseAuction(auctionService.getAuctions(requestContext.getUsername()));
        return ResponseEntity.ok(responseAuction);
    }

    /**
     * This Api will used to bid againt an item of an Auction.
     *
     * @param requestUserBid
     *            : Input will contain auctionId, ItemId and userBid amount.
     *
     * @return ResponseUserBid will contain userBid amount and currentBid amount in the response. List<Bid> is not a
     *         part of response of this Api and hence will not be populated
     */
    @PostMapping(value = "/bid/auctions")
    public ResponseEntity<ResponseUserBid> applyBid(@RequestBody RequestUserBid requestUserBid) {
        // TODO: Admin can bid on behalf of an User.

        return ResponseEntity.ok(biddingService.applyBid(requestUserBid, requestContext.getUsername()));
    }

    /**
     * This Api is used to fetch list of bids made by a user on an auction(includes any item)
     *
     * @param auctionId
     *
     * @return ResponseUserBid will contain List<Bid> which will be populated as response. userBid and currentBid will
     *         be empty as they are not a part of response for this Api.
     */
    @GetMapping(value = "/bid/auctions/{auctionId}")
    public ResponseEntity<ResponseUserBid> getUserBids(@PathVariable(required = true) String auctionId) {
        return ResponseEntity.ok(new ResponseUserBid(biddingService.getUserBidsViaAuctionId(auctionId, requestContext.getUsername())));
    }

}
