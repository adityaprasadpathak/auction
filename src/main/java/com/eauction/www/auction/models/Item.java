package com.eauction.www.auction.models;

import com.eauction.www.auction.dto.ItemEntity;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {

    public Item(ItemEntity itemEntity) {
        // Safe check for lazy-loaded or null entities
        if (itemEntity.getAuctionEntity() != null) {
            this.auctionId = itemEntity.getAuctionEntity().getAuctionId();
        }
        this.itemDescription = itemEntity.getItemDescription();
        this.itemName = itemEntity.getItemName();
        this.itemId = itemEntity.getItemId();
        this.itemCount = itemEntity.getItemCount();
        this.itemStartPrice = itemEntity.getItemStartPrice();
    }

    private String itemId;

    @NotBlank(message = "Item name is required")
    @Size(min = 2, max = 100, message = "Item name must be between 2 and 100 characters")
    private String itemName;

    @NotBlank(message = "Item description is required")
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String itemDescription;

    @NotNull(message = "Starting price is required")
    @DecimalMin(value = "0.01", message = "Starting price must be at least 0.01")
    private Double itemStartPrice;

    @NotNull(message = "Item count is required")
    @Min(value = 1, message = "Item count must be at least 1")
    @Max(value = 1000, message = "You cannot auction more than 1000 units of a single item")
    private Integer itemCount;

    private String auctionId;
}
