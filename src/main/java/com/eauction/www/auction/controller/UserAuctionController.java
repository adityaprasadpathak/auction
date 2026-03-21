package com.eauction.www.auction.controller;

import com.eauction.www.auction.exception.AuctionServiceException;
import com.eauction.www.auction.models.RequestUserBid;
import com.eauction.www.auction.models.ResponseAuction;
import com.eauction.www.auction.models.ResponseUserBid;
import com.eauction.www.auction.models.ServiceErrorCode;
import com.eauction.www.auction.service.AuctionService;
import com.eauction.www.auction.service.BiddingService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * <h2>UserAuctionController</h2>
 *
 * <p>
 * This controller contains APIs that are <b>NOT open</b> and therefore require a valid
 * <b>JWT Authentication Token</b> to be accessed.
 * </p>
 *
 * <p>
 * As the name suggests, this controller exposes APIs intended for <b>authenticated users only</b>.
 * All incoming requests are expected to be authenticated via Spring Security, and user details
 * are extracted from the {@link Authentication} object.
 * </p>
 *
 * <p>
 * NOTE:
 * <ul>
 *     <li>Authentication is handled via JWT and populated in the Security Context by a filter.</li>
 *     <li>User identity (username) is retrieved using {@code Authentication.getName()}.</li>
 *     <li>No custom RequestContext is used; instead, Spring Security context is leveraged.</li>
 * </ul>
 * </p>
 */
@CrossOrigin
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserAuctionController {

    private final AuctionService auctionService;
    private final BiddingService biddingService;

    /**
     * <h3>Get Auctions for Logged-in User</h3>
     *
     * <p>
     * This API returns auctions created by the currently authenticated user.
     * The results are sorted in <b>descending order of auction start time</b>.
     * </p>
     *
     * <p>
     * Behavior:
     * <ul>
     *     <li>If <b>auctionId</b> is provided → returns only that specific auction.</li>
     *     <li>If <b>auctionId</b> is NOT provided → returns a list of all auctions created by the user.</li>
     * </ul>
     * </p>
     *
     * <p>
     * Response Structure:
     * <ul>
     *     <li>If auctionId is provided → {@code ResponseAuction.auction} is populated.</li>
     *     <li>If auctionId is NOT provided → {@code ResponseAuction.auctions} list is populated.</li>
     * </ul>
     * </p>
     *
     * @param auctionId optional auction identifier to filter a specific auction
     * @param authentication Spring Security authentication object containing logged-in user details
     * @return ResponseEntity containing {@link ResponseAuction}
     */
    @GetMapping("/auctions")
    public ResponseEntity<ResponseAuction> getAuctions(
            @RequestParam(required = false) String auctionId,
            Authentication authentication) {

        String username = authentication.getName();

        ResponseAuction responseAuction = (auctionId != null)
                ? new ResponseAuction(
                auctionService.getAuctionViaIdAndUsername(auctionId, username))
                : new ResponseAuction(
                auctionService.getAuctions(username));

        return ResponseEntity.ok(responseAuction);
    }

    /**
     * <h3>Apply Bid on Auction Item</h3>
     *
     * <p>
     * This API allows an authenticated user to place a bid on a specific item within an auction.
     * </p>
     *
     * <p>
     * Input Requirements:
     * <ul>
     *     <li>Auction ID</li>
     *     <li>Item ID</li>
     *     <li>User's bid amount</li>
     * </ul>
     * </p>
     *
     * <p>
     * Behavior:
     * <ul>
     *     <li>Validates whether the auction is currently <b>active</b>.</li>
     *     <li>If active → bid is accepted and processed.</li>
     *     <li>If not active → throws {@link AuctionServiceException}.</li>
     * </ul>
     * </p>
     *
     * <p>
     * Response:
     * <ul>
     *     <li>Contains user's bid amount and current highest bid.</li>
     *     <li>Does NOT include full bid history.</li>
     * </ul>
     * </p>
     *
     * <p>
     * NOTE:
     * <ul>
     *     <li>Currently only logged-in users can bid for themselves.</li>
     *     <li>Future enhancement: Admin can bid on behalf of another user.</li>
     * </ul>
     * </p>
     *
     * @param requestUserBid request payload containing auctionId, itemId and bid amount
     * @param authentication Spring Security authentication object
     * @return ResponseEntity containing {@link ResponseUserBid}
     */
    @PostMapping("/bid/auctions")
    public ResponseEntity<ResponseUserBid> applyBid(
            @RequestBody RequestUserBid requestUserBid,
            Authentication authentication) {

        String username = authentication.getName();

        if (auctionService.isAuctionActive(requestUserBid.getAuctionId())) {
            return ResponseEntity.ok(
                    biddingService.applyBid(requestUserBid, username)
            );
        } else {
            throw new AuctionServiceException(ServiceErrorCode.AUCTION_NOT_ACTIVE);
        }
    }

    /**
     * <h3>Get User Bids for an Auction</h3>
     *
     * <p>
     * This API retrieves all bids placed by the currently authenticated user
     * for a given auction (across all items in that auction).
     * </p>
     *
     * <p>
     * Behavior:
     * <ul>
     *     <li>Fetches all bids made by the user for the specified auction.</li>
     *     <li>Includes bids across all items within the auction.</li>
     * </ul>
     * </p>
     *
     * <p>
     * Response:
     * <ul>
     *     <li>Returns a list of bids.</li>
     *     <li>{@code userBid} and {@code currentBid} fields are NOT populated.</li>
     * </ul>
     * </p>
     *
     * @param auctionId auction identifier
     * @param authentication Spring Security authentication object
     * @return ResponseEntity containing {@link ResponseUserBid}
     */
    @GetMapping("/bid/auctions/{auctionId}")
    public ResponseEntity<ResponseUserBid> getUserBids(
            @PathVariable String auctionId,
            Authentication authentication) {

        String username = authentication.getName();

        return ResponseEntity.ok(
                new ResponseUserBid(
                        biddingService.getUserBidsViaAuctionId(auctionId, username)
                )
        );
    }
}