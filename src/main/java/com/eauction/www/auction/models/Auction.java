package com.eauction.www.auction.models;

import com.eauction.www.auction.dto.AuctionEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Auction {

    /**
     * Copy Constructor for converting an {@code AuctionEntity} to an {@code Auction}.
     * <p>
     * This constructor creates a new {@code Auction} object by extracting relevant information
     * from the provided {@code AuctionEntity}. It is particularly useful when transferring
     * data between the database entities and the business model objects.
     * </p>
     *
     * @param auctionEntity The {@code AuctionEntity} to be converted.
     */
    public Auction(AuctionEntity auctionEntity) {
        this.auctionId = auctionEntity.getAuctionId();
        this.auctionName = auctionEntity.getAuctionName();
        this.createdBy = auctionEntity.getCreatedBy();
        this.startTimestamp = auctionEntity.getStartTimestamp();
        this.stopTimestamp = auctionEntity.getStopTimestamp();
        this.isCancelled = auctionEntity.isCancelled();
        this.isResultDeclared = auctionEntity.isResultDeclared();
        this.username = auctionEntity.getUsername();
        this.createdTimestamp = auctionEntity.getCreatedTimestamp();
        this.status = auctionEntity.getStatus();
        this.items = auctionEntity.getItems().stream().map(Item::new).collect(Collectors.toList());
    }


    private String username;
    private String createdBy;
    private String auctionId;
    private String auctionName;
    private String auctionDescription;
    private List<Item> items;
    private Long startTimestamp;
    private Long stopTimestamp;
    private Long createdTimestamp;
    private AuctionStatus status;
    private boolean isResultDeclared;
    private boolean isCancelled;
    private String reasonForCancellation;
    private Long timeOfCancellation;
}
