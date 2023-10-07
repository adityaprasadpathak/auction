package com.eauction.www.auction.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

public class Item {

    private String ItemId;
    private String ItemName;
    private String ItemDescription;
    private Double ItemStartPrice;
    private Integer itemCount;
    private String auctionId;

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getItemDescription() {
        return ItemDescription;
    }

    public void setItemDescription(String itemDescription) {
        ItemDescription = itemDescription;
    }

    public Double getItemStartPrice() {
        return ItemStartPrice;
    }

    public void setItemStartPrice(Double itemStartPrice) {
        ItemStartPrice = itemStartPrice;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public String getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(String auctionId) {
        this.auctionId = auctionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Item))
            return false;
        Item item = (Item) o;
        return Objects.equals(ItemId, item.ItemId) && Objects.equals(ItemName, item.ItemName)
                && Objects.equals(ItemDescription, item.ItemDescription)
                && Objects.equals(ItemStartPrice, item.ItemStartPrice) && Objects.equals(itemCount, item.itemCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ItemId, ItemName, ItemDescription, ItemStartPrice, itemCount);
    }

    @Override
    public String toString() {
        return "Item{" + "ItemId='" + ItemId + '\'' + ", ItemName='" + ItemName + '\'' + ", ItemDescription='"
                + ItemDescription + '\'' + ", ItemStartPrice='" + ItemStartPrice + '\'' + ", itemCount=" + itemCount
                + '}';
    }
}
