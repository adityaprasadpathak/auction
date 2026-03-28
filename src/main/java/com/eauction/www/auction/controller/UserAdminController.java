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

    public final AuctionService auctionService;

    private final BiddingService biddingService;
//
//    @GetMapping("/auctions")
//    public ResponseEntity<ResponseAuction> getAuctions(
//            @RequestParam(required = false) AuctionStatus auctionStatus,
//            Authentication authentication) {
//
//        String username = authentication.getName();
//        ResponseAuction responseAuction;
//        if (auctionStatus != null) {
//            responseAuction  = new ResponseAuction(auctionService.getAuctionsViaAuctionStatus(auctionStatus));
//        } else {
//            responseAuction = new ResponseAuction(auctionService.getAuctions());
//        }
//
//
//        return ResponseEntity.ok(responseAuction);
//    }

    /*@GetMapping("/auctions/{auctionId}")
    public ResponseEntity<ResponseAuction> getAuctions(
            @PathVariable String auctionId,
            Authentication authentication) {

        String username = authentication.getName();

        ResponseAuction responseAuction = new ResponseAuction(auctionService.getAuctionViaAuctionId(auctionId));

        return ResponseEntity.ok(responseAuction);
    }*/



}
