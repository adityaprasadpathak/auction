package com.eauction.www.auction.dto;

import com.eauction.www.auction.models.Bid;
import com.eauction.www.auction.util.Utility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Bids")
public class BidEntity {

    public BidEntity(Bid bid) {
        this.auctionId = bid.getAuctionId();
        this.bidTime = bid.getBidTime();
        this.bid = bid.getBid();
        this.id = bid.getBidId();
        this.itemId = bid.getItemId();
        this.username = bid.getUsername();
        this.bidValueAtThatTime = bid.getBidValueAtThatTime();
    }

    @Id
    private String id;

    @Column(name = "auction_id")
    private String auctionId;

    @Column(name = "item_id")
    private String itemId;

    @Column(name = "bid")
    private Integer bid;

    @Column(name = "bid_value_at_that_time")
    private Integer bidValueAtThatTime;

    @Column(name = "username")
    private String username;

    @Column(name = "bid_time")
    private Long bidTime;

    @PrePersist
    protected void onCreate() {
        id = Utility.generateUniqueBidId();
        bidTime = System.currentTimeMillis();
    }
}
