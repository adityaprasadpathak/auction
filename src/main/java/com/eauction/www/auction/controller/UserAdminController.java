package com.eauction.www.auction.controller;

import com.eauction.www.auction.models.Auction;
import com.eauction.www.auction.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * As this is not open controller, hence all Api's here in this controller needs AuthToken(JWT token) to make a call.
 * UserAdminController as name suggest, will contain common Api's which needs to be called by both user and Admin.
 */
@RestController
@RequestMapping("/useradmin")
public class UserAdminController {

    @Autowired
    public AuctionService auctionService;

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

}
