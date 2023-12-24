package com.eauction.www.auction.dto;

import com.eauction.www.auction.models.AuctionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "auction_status")
public class AuctionStatusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private Long statusId;

    @Column(name = "name", unique = true)
    @Enumerated(EnumType.STRING)
    private AuctionStatus name;

}
