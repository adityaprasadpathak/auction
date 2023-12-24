package com.eauction.www.auction.dto;

import com.eauction.www.auction.models.Auction;
import com.eauction.www.auction.models.AuctionStatus;
import com.eauction.www.auction.util.Utility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "auctions")
public class AuctionEntity {

    // Copy Constructor for converting Auction to AuctionEntity
    public AuctionEntity(Auction auction) {
        this.auctionDescription = auction.getAuctionDescription();
        this.auctionName = auction.getAuctionName();
        this.auctionId = auction.getAuctionId();
        this.createdTimestamp = auction.getCreatedTimestamp();
        this.username = auction.getUsername();
        this.reasonForCancellation = auction.getReasonForCancellation();
        this.status = auction.getStatus();
        this.isCancelled = auction.isCancelled();
        this.isResultDeclared = auction.isResultDeclared();
        this.createdBy = auction.getCreatedBy();
        this.startTimestamp = auction.getStartTimestamp();
        this.stopTimestamp = auction.getStopTimestamp();
        this.items = auction.getItems().stream().map(item -> new ItemEntity(item,this)).collect(Collectors.toList());

    }


    @Id
    @Column(name = "auction_id")
    private String auctionId;

    @Column(name = "username")
    private String username;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "auction_name")
    private String auctionName;

    @Column(name = "auction_description")
    private String auctionDescription;

    @OneToMany(mappedBy = "auctionEntity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ItemEntity> items;

    @Column(name = "start_timestamp")
    private Long startTimestamp;

    @Column(name = "stop_timestamp")
    private Long stopTimestamp;

    @Column(name = "created_timestamp")
    private Long createdTimestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AuctionStatus status;

    @Column(name = "is_result_declared")
    private boolean isResultDeclared;

    @Column(name = "is_cancelled")
    private boolean isCancelled;

    @Column(name = "reason_for_cancellation")
    private String reasonForCancellation;

    @Column(name = "time_of_cancellation")
    private Long timeOfCancellation;

    @PrePersist
    protected void onCreate() {
        auctionId = Utility.generateUniqueAuctionId();
        createdTimestamp = System.currentTimeMillis();
    }



    // Constructors, getters, and setters
}
