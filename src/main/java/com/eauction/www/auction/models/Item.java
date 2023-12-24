package com.eauction.www.auction.models;

import com.eauction.www.auction.dto.ItemEntity;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor
public class Item {

    public Item(ItemEntity itemEntity) {
        this.auctionId = itemEntity.getAuctionEntity().getAuctionId();
        this.itemDescription = itemEntity.getItemDescription();
        this.itemName = itemEntity.getItemName();
        this.itemId = itemEntity.getItemId();
        this.itemCount = itemEntity.getItemCount();
        this.itemStartPrice = itemEntity.getItemStartPrice();
    }

        private String itemId;
        private String itemName;
        private String itemDescription;
        private Double itemStartPrice;
        private Integer itemCount;
        private String auctionId;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public Double getItemStartPrice() {
        return itemStartPrice;
    }

    public void setItemStartPrice(Double itemStartPrice) {
        this.itemStartPrice = itemStartPrice;
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
        return Objects.equals(itemId, item.itemId) && Objects.equals(itemName, item.itemName)
                && Objects.equals(itemDescription, item.itemDescription)
                && Objects.equals(itemStartPrice, item.itemStartPrice) && Objects.equals(itemCount, item.itemCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, itemName, itemDescription, itemStartPrice, itemCount);
    }

    @Override
    public String toString() {
        return "Item{" + "ItemId='" + itemId + '\'' + ", ItemName='" + itemName + '\'' + ", ItemDescription='"
                + itemDescription + '\'' + ", ItemStartPrice='" + itemStartPrice + '\'' + ", itemCount=" + itemCount
                + '}';
    }
}
