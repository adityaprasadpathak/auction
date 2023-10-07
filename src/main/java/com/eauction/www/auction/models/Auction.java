package com.eauction.www.auction.models;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
public class Auction {

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

    public AuctionStatus getStatus() {
        return status;
    }

    public void setStatus(AuctionStatus status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isResultDeclared() {
        return isResultDeclared;
    }

    public void setResultDeclared(boolean resultDeclared) {
        isResultDeclared = resultDeclared;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    public String getReasonForCancellation() {
        return reasonForCancellation;
    }

    public void setReasonForCancellation(String reasonForCancellation) {
        this.reasonForCancellation = reasonForCancellation;
    }

    public Long getTimeOfCancellation() {
        return timeOfCancellation;
    }

    public void setTimeOfCancellation(Long timeOfCancellation) {
        this.timeOfCancellation = timeOfCancellation;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Auction))
            return false;
        Auction auction = (Auction) o;
        return Objects.equals(auctionId, auction.auctionId) && Objects.equals(auctionName, auction.auctionName)
                && Objects.equals(auctionDescription, auction.auctionDescription)
                && Objects.equals(items, auction.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(auctionId, auctionName, auctionDescription, items);
    }

    @Override
    public String toString() {
        return "Auction{" + "auctionId='" + auctionId + '\'' + ", auctionName='" + auctionName + '\''
                + ", auctionDescription='" + auctionDescription + '\'' + ", items=" + items + '}';
    }
}
