package com.eauction.www.auction.util;

import com.eauction.www.auction.dto.AuctionEntity;
import com.eauction.www.auction.dto.BidEntity;
import com.eauction.www.auction.models.Auction;
import com.eauction.www.auction.models.Bid;
import com.google.gson.Gson;

import java.util.List;
import java.util.stream.Collectors;

public final class ConverterUtility {

    public static List<Auction> convertToAuctionList(List<AuctionEntity> auctionEntities) {
        return auctionEntities.stream()
                .map(Auction::new) // This will use the copy constructor
                .collect(Collectors.toList());
    }


    public static List<AuctionEntity> convertToAuctionEntityList(List<Auction> auctions) {
        return auctions.stream()
                .map(AuctionEntity::new) // This will use the copy constructor
                .collect(Collectors.toList());
    }

    public static List<Bid> convertToBidList(List<BidEntity> bidEntities) {
        return bidEntities.stream()
                .map(Bid::new) // This will use the copy constructor
                .collect(Collectors.toList());
    }

    public static Bid convertToBid(BidEntity bidEntity) {
        return new Bid(bidEntity); // This will use the copy constructor
    }



}
