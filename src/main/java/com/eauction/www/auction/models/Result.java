package com.eauction.www.auction.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Result {

    private String itemId;
    private String bidder;

    private Integer bidAmount;
}
