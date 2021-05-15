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


@RestController
@RequestMapping("/users")
public class UserAuctionController {


    @Autowired
    public AuctionService auctionService;

    @Autowired
    public RequestContext requestContext;

    @Autowired
    BiddingService biddingService;




    @GetMapping(value = "/auctions")
    public ResponseEntity<ResponseAuction> getAuctions(@RequestParam(required = false) String auctionId )
    {

        ResponseAuction responseAuction =  auctionId!=null?
                new ResponseAuction(auctionService.getAuctions(requestContext.getUsername(),auctionId)):
                new ResponseAuction(auctionService.getAuctions(requestContext.getUsername()));
        return ResponseEntity.ok(responseAuction);
    }



    @PostMapping(value = "/bid/auctions/{auctionId}")
    public ResponseEntity<ResponseUserBid> getAuctions(@PathVariable String auctionId , @RequestBody RequestUserBid requestUserBid )
    {
        return ResponseEntity.ok(biddingService.applyBid(requestUserBid,requestContext.getUsername()));
    }

    @PostMapping(value = "/bid/auctions")
    public ResponseEntity<ResponseUserBid> applyBid(@RequestBody RequestUserBid requestUserBid )
    {
        //TODO: Admin can bid on behalf of an User.

        return ResponseEntity.ok(biddingService.applyBid(requestUserBid,requestContext.getUsername()));
    }

    @GetMapping(value = "/bid/auctions/{auctionId}")
    public ResponseEntity<ResponseUserBid> getUserBids(@PathVariable(required = true) String auctionId )
    {
        return ResponseEntity.ok(new ResponseUserBid(biddingService.getBids(auctionId,requestContext.getUsername())));
    }


}
