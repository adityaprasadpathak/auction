package com.eauction.www.auction.controller;

import com.eauction.www.auction.dto.BidEntity;
import com.eauction.www.auction.exception.AuctionServiceException;
import com.eauction.www.auction.models.*;
import com.eauction.www.auction.search.models.AuctionSearchCriteria;
import com.eauction.www.auction.service.AuctionService;
import com.eauction.www.auction.service.BiddingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RestControllerAdvice
@RequestMapping("/auctions")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuctionController {

    private final AuctionService auctionService;
    private final BiddingService biddingService;


    @PostMapping
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<Auction> createAuction(@RequestBody Auction newAuction, Authentication authentication) {
        Auction createdAuction = auctionService.createAuction(newAuction, authentication);
        return ResponseEntity.ok(createdAuction);
    }

    @PutMapping("/{auctionId}/publish")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Auction> publishAuction(@PathVariable String auctionId) {
        Auction publishedAuction = auctionService.publishAuction(auctionId);
        return ResponseEntity.ok(publishedAuction);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Page<Auction>> getAuctions(@Valid AuctionSearchCriteria criteria) {
        return ResponseEntity.ok(auctionService.getAuctions(criteria));
    }

    @GetMapping("/{auctionId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Auction> getAuction(@PathVariable String auctionId) {
        return ResponseEntity.ok(auctionService.getAuctionViaAuctionId(auctionId));
    }

    @PutMapping("/{auctionId}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<Auction> updateAuction(@PathVariable String auctionId, @RequestBody Auction updatedAuction, Authentication authentication) {
        auctionService.updateAuction(auctionId, updatedAuction, authentication);
        return ResponseEntity.ok(updatedAuction);
    }

    @DeleteMapping("/{auctionId}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<Void> deleteAuction(@PathVariable String auctionId, Authentication authentication) {
        auctionService.deleteAuction(auctionId, authentication);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{auctionId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public Auction approveAuction(@PathVariable String auctionId) {
        return auctionService.approveAuction(auctionId);
    }

    @PutMapping(value = "/{auctionId}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public Auction rejectAuction(@PathVariable String auctionId, @RequestParam (required = false, defaultValue = "Not Viable") String reason) {
        return auctionService.rejectAuction(auctionId, reason);
    }

    @PutMapping("/{auctionId}/temporarily-stop")
    @PreAuthorize("hasRole('ADMIN')")
    public Auction temporarilyStopAuction(@PathVariable String auctionId) {
        return auctionService.temporarilyStopAuction(auctionId);
    }

    @PutMapping("/{auctionId}/resume")
    @PreAuthorize("hasRole('ADMIN')")
    public Auction resumeAuction(@PathVariable String auctionId) {
        return auctionService.resumeAuction(auctionId);
    }

    @PostMapping("/{auctionId}/items/{itemId}/bids")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BidResponse> placeBid(
            @PathVariable String auctionId,
            @PathVariable String itemId,
            @RequestBody RequestUserBid requestUserBid,
            Authentication authentication) {

        // 1. Consistency Check: Ensure URL IDs match Request Body IDs
        // This prevents "Cross-Site Request Forgery" where a user submits a bid for A
        // but the URL says B.
        /*if (!auctionId.equals(requestUserBid.getAuctionId()) || !itemId.equals(requestUserBid.getItemId())) {
            throw new AuctionServiceException("URL parameters must match request body", ServiceErrorCode.INVALID_INPUT);
        }*/
        requestUserBid.setAuctionId(auctionId);
        requestUserBid.setItemId(itemId);
        BidResponse response = biddingService.applyBid(requestUserBid, authentication);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{auctionId}/items/{itemId}/bids")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<BidResponse> getBid(
            @PathVariable String auctionId,
            @PathVariable String itemId,
            @RequestParam (required = false, defaultValue = "false") boolean latestOnly,
            Authentication authentication) {

        if (latestOnly) {
            Bid latestBid = biddingService.getLatestBidViaAuctionIdAndItemId(auctionId, itemId);
            return ResponseEntity.ok(new BidResponse(latestBid));

        }

        List<Bid> bids = biddingService.getBidsViaAuctionIdAndItemId(auctionId, itemId, authentication);
        return ResponseEntity.ok(new BidResponse(bids));
    }


    @DeleteMapping("/{auctionId}/items/{itemId}/bids/{bidId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBid(
            @PathVariable String auctionId,
            @PathVariable String itemId,
            @PathVariable String bidId) {

        biddingService.deleteBid(auctionId, itemId, bidId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{auctionId}/result")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<Result> getResult(@PathVariable String auctionId) {

        if (!auctionService.isResultDeclared(auctionId)) {
            throw new AuctionServiceException(
                    "Auction is still active",
                    ServiceErrorCode.RESULT_NOT_DECLARED
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
