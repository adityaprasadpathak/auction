package com.eauction.www.auction.models;

import java.util.List;
import java.util.Objects;

public class Auction {

    private String userId;
    private String auctionId;
    private String auctionName;
    private String auctionDescription;
    private List<Item> items;
    private Long startTimestamp;
    private Long stopTimestamp;
    private String status;


    public String getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(String auctionId) {
        this.auctionId = auctionId;
    }

    public String getAuctionName() {
        return auctionName;
    }

    public void setAuctionName(String auctionName) {
        this.auctionName = auctionName;
    }

    public String getAuctionDescription() {
        return auctionDescription;
    }

    public void setAuctionDescription(String auctionDescription) {
        this.auctionDescription = auctionDescription;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Long getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(Long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public Long getStopTimestamp() {
        return stopTimestamp;
    }

    public void setStopTimestamp(Long stopTimestamp) {
        this.stopTimestamp = stopTimestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Auction)) return false;
        Auction auction = (Auction) o;
        return Objects.equals(auctionId, auction.auctionId) &&
                Objects.equals(auctionName, auction.auctionName) &&
                Objects.equals(auctionDescription, auction.auctionDescription) &&
                Objects.equals(items, auction.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(auctionId, auctionName, auctionDescription, items);
    }

    @Override
    public String toString() {
        return "Auction{" +
                "auctionId='" + auctionId + '\'' +
                ", auctionName='" + auctionName + '\'' +
                ", auctionDescription='" + auctionDescription + '\'' +
                ", items=" + items +
                '}';
    }
}
