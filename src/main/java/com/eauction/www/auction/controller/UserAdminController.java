package com.eauction.www.auction.controller;

import com.eauction.www.auction.dto.UserEntity;
import com.eauction.www.auction.models.Auction;
import com.eauction.www.auction.models.UserRegistration;
import com.eauction.www.auction.repo.UserRepository;
import com.eauction.www.auction.security.RequestContext;
import com.eauction.www.auction.service.AuctionService;
import com.eauction.www.auction.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * As this is not open controller, hence all Api's here in this controller needs AuthToken(JWT token) to make a call.
 * UserAdminController as name suggest will contains common Api's which needs to be called by both user as well as by Admin.
 */
@RestController
@RequestMapping("/useradmin")
public class UserAdminController {

    @Autowired
    public AuctionService auctionService;

    /**
     * This Api is used to create auction.
     * Auction will always have an owner, which will be the user who created it.
     *
     * An Admin can also create a auction on behalf of an user.
     * Remember an Admin can never be an owner of the auction
     *
     * @param auction
     * @return
     */
    @PostMapping(value = "/auction")
    public Auction createAuction(@RequestBody Auction auction){
        return auctionService.createAuction(auction);
    }



}
