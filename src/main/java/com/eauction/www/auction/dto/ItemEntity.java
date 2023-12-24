package com.eauction.www.auction.dto;

import com.eauction.www.auction.models.Item;
import com.eauction.www.auction.util.Utility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "items")
public class ItemEntity {


    // Copy Constructor for converting Item to ItemEntity
    public ItemEntity(Item item, AuctionEntity auctionEntity) {
        this.itemDescription = item.getItemDescription();
        this.itemId = item.getItemId();
        this.itemName = item.getItemName();
        this.itemStartPrice = item.getItemStartPrice();
        this.itemCount = item.getItemCount();
        this.auctionEntity = auctionEntity;
    }

    @Id
    @Column(name = "item_id")
    private String itemId;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "item_description")
    private String itemDescription;

    @Column(name = "item_start_price")
    private Double itemStartPrice;

    @Column(name = "item_count")
    private Integer itemCount;

    @ManyToOne
    @JoinColumn(name = "auction_id")
    private AuctionEntity auctionEntity;

    @PrePersist
    protected void onCreate() {
        itemId = Utility.generateUniqueItemId();
    }
}
