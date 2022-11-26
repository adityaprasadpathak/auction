package com.eauction.www.auction.dto;

import com.eauction.www.auction.models.Item;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

public class AuctionEntity {

    @Id
    private String auctionId;

    @Column(nullable = false)
    private String auctionName;

    @Column(nullable = false)
    private String auctionDescription;

    private List<Item> items;

    @Column(nullable = false)
    private Long startTimestamp;

    @Column(nullable = false)
    private Long stopTimestamp;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Long lastModifiedOn;

    @Column(nullable = false)
    private String createdBy;
}
