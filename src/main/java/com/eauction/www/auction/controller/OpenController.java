package com.eauction.www.auction.controller;

import com.eauction.www.auction.dto.BidEntity;
import com.eauction.www.auction.dto.UserEntity;
import com.eauction.www.auction.exception.AuctionServiceException;
import com.eauction.www.auction.models.*;
import com.eauction.www.auction.repository.UserRepository;
import com.eauction.www.auction.request.models.AuthenticateRequest;
import com.eauction.www.auction.response.models.AuthenticateResponse;
import com.eauction.www.auction.security.RequestContext;
import com.eauction.www.auction.service.AuctionService;
import com.eauction.www.auction.service.BiddingService;
import com.eauction.www.auction.service.MyUserDetailsService;
import com.eauction.www.auction.util.JwtUtil;
import com.eauction.www.auction.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This controller contains Api's which needs no AuthToken(JWT token) to call.
 * Therefore, they are termed as open Api's, and hence controller name.
 */
@RestController
public class OpenController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    MyUserDetailsService myUserDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RequestContext requestContext;


    @Autowired
    AuctionService auctionService;

    @Autowired
    BiddingService biddingService;

    /**
     * Test API to get Sample Auction response. Will be deleting it after
     *
     * @return
     */
    @Deprecated
    @GetMapping("/auctions")
    public Auction getAuctions() {
        return Utility.createSampleAuction();
    }

    /**
     * This Api is used by public to signUp in eAuction. Anyone can use this API to register them self, general signup
     * will always create a user(not Admin).
     * <p>
     * <p>
     * Same API can be used by an Admin to create User(or signup on behalf of an user), and later his username/password
     * can be given to him. Remember an Admin can create an AdminUser too. if Admin going to use this Api to create user
     * or admin, he is supposed to pass AuthToken (JWT token), otherwise call considered as a normal user signup.
     *
     * @param userRegistration
     *
     * @return
     */
    @PostMapping("/registration")
    public String registration(@RequestBody UserRegistration userRegistration) {

        UserEntity userEntity = userRepository.save(Utility.convertToUserEntity(userRegistration, passwordEncoder,
                Boolean.FALSE, requestContext.getUsername()));
        if (userEntity != null) {
            return userEntity.getUsername();
        } else {
            throw new RuntimeException("Unable To Register");
        }

    }

    /**
     * This Api is to login in eAuction (via username and password) A successful login will return a JWT token valid for
     * 10 days. this token can be used to call subsequent Api's
     *
     * @param authenticateRequest
     *
     * @return
     *
     * @throws Exception
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticateResponse> authenticate(@RequestBody AuthenticateRequest authenticateRequest)
            throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticateRequest.getUsername(), authenticateRequest.getPassword()));
        } catch (BadCredentialsException bce) {
            throw new RuntimeException("Bad Credential", bce);
        } catch (LockedException le) {
            throw new RuntimeException("User Account Is Locked.Please contact administrator", le);
        }

        UserDetails userDetails = myUserDetailsService.loadUserByUsername(authenticateRequest.getUsername());
        String jwtToken = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticateResponse(jwtToken));
    }

    @GetMapping("/auction/{auctionId}/result")
    public List<Result> getResult(@PathVariable String auctionId) {

        List<Result> auctionResult = new ArrayList<>();
        if (auctionService.isAuctionFinished(auctionId)) {
             List<Item> auctionItems = auctionService.getItemsforAuction(auctionId);
             auctionItems.parallelStream().forEach(item -> {
                 BidEntity bidEntity = biddingService.getCurrentHighestBidder(auctionId,item.getItemId());
                 if (null != bidEntity) {
                     Result result = Result.builder()
                             .itemId(bidEntity.getItemId())
                             .bidder(bidEntity.getUsername())
                             .bidAmount(bidEntity.getBid())
                             .build();
                     auctionResult.add(result);
                 }
             });
             return auctionResult;

        } else {
            throw new AuctionServiceException(
                    "Auction is Still Active, result will be declared once Auction is Finished"
                    ,ServiceErrorCode.AUCTION_NOT_FINISHED);
        }


        // if Auction is finished, fetch top 3 bids
    }

}
