package com.eauction.www.auction.controller;

import com.eauction.www.auction.dto.BidEntity;
import com.eauction.www.auction.dto.UserEntity;
import com.eauction.www.auction.exception.AuctionServiceException;
import com.eauction.www.auction.models.*;
import com.eauction.www.auction.repository.UserRepository;
import com.eauction.www.auction.request.models.AuthenticateRequest;
import com.eauction.www.auction.response.models.AuthenticateResponse;
import com.eauction.www.auction.service.AuctionService;
import com.eauction.www.auction.service.BiddingService;
import com.eauction.www.auction.service.MyUserDetailsService;
import com.eauction.www.auction.util.JwtUtil;
import com.eauction.www.auction.util.Utility;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@Slf4j
public class OpenController {

    private final AuthenticationManager authenticationManager;
    private final MyUserDetailsService myUserDetailsService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuctionService auctionService;
    private final BiddingService biddingService;

    @GetMapping("/auctions")
    public Auction getAuctions() {
        return Utility.createSampleAuction();
    }

    @PostMapping("/registration")
    public ResponseEntity<String> registration(
            @RequestBody UserRegistration userRegistration,
            Authentication authentication) {

        try {
            String createdBy = null;

            if (authentication != null && authentication.isAuthenticated()
                    && !"anonymousUser".equals(authentication.getPrincipal())) {
                createdBy = authentication.getName();
            }

            UserEntity userEntity = userRepository.save(
                    Utility.convertToUserEntity(
                            userRegistration,
                            passwordEncoder,
                            false,
                            createdBy
                    )
            );

            return ResponseEntity.ok(userEntity.getUsername());

        } catch (DataIntegrityViolationException dive) {
            log.warn("Attempt to register with existing username: {}", userRegistration.getUserName());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Username already exists");
        }
        catch (Exception e) {
            log.error("Error during user registration: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to register user");
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticateRequest request) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");

        } catch (LockedException e) {
            return ResponseEntity.status(HttpStatus.LOCKED)
                    .body("User account is locked");
        }

        UserDetails userDetails =
                myUserDetailsService.loadUserByUsername(request.getUsername());

        String jwtToken = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticateResponse(jwtToken));
    }

    @GetMapping("/auction/{auctionId}/result")
    public List<Result> getResult(@PathVariable String auctionId) {

        if (!auctionService.isAuctionFinished(auctionId)) {
            throw new AuctionServiceException(
                    "Auction is still active",
                    ServiceErrorCode.AUCTION_NOT_FINISHED
            );
        }

        List<Item> auctionItems =
                auctionService.getItemsForAuction(auctionId);

        return auctionItems.stream()
                .map(item -> {
                    BidEntity bidEntity =
                            biddingService.getCurrentHighestBidder(
                                    auctionId,
                                    item.getItemId()
                            );

                    if (bidEntity == null) return null;

                    return Result.builder()
                            .itemId(bidEntity.getItemId())
                            .bidder(bidEntity.getUsername())
                            .bidAmount(bidEntity.getBid())
                            .build();
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}