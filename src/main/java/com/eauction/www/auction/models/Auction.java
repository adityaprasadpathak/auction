package com.eauction.www.auction.models;

import com.eauction.www.auction.dto.AuctionEntity;
import com.eauction.www.auction.validator.annotation.ValidAuctionDuration;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ValidAuctionDuration
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
        this.items = auctionEntity.getItems().stream().map(Item::new).toList();
        this.isTemporarilyStopped = auctionEntity.isTemporarilyStopped();
        this.reasonForCancellation = auctionEntity.getReasonForCancellation();
    }


    @NotBlank(message = "Username is required")
    private String username;

    private String createdBy;

    private String auctionId;

    @NotBlank(message = "Auction name cannot be empty")
    @Size(min = 3, max = 100, message = "Auction name must be between 3 and 100 characters")
    private String auctionName;

    @Size(max = 500, message = "Description is too long")
    private String auctionDescription;

    @NotEmpty(message = "An auction must have at least one item")
    @Max(value = 5, message = "An auction cannot have more than 5 items")
    @Valid
    private List<Item> items;

    @NotNull(message = "Start time is required")
    @Positive(message = "Start timestamp must be a valid positive number")
    private Long startTimestamp;

    @NotNull(message = "Stop time is required")
    @Positive(message = "Stop timestamp must be a valid positive number")
    private Long stopTimestamp;

    private Long createdTimestamp;

    private AuctionStatus status;

    private boolean isResultDeclared;
    private boolean isCancelled;
    private String reasonForCancellation;
    private Long timeOfCancellation;
    private boolean isTemporarilyStopped;
}
