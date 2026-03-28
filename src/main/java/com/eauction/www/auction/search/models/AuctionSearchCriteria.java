package com.eauction.www.auction.search.models;

import com.eauction.www.auction.models.AuctionStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuctionSearchCriteria {

    @Min(value = 1, message = "Limit must be at least 1")
    @Max(value = 100, message = "Limit cannot exceed 100")
    private Integer limit = 10;

    @PositiveOrZero(message = "Offset cannot be negative")
    private Integer offset = 0;

    private AuctionStatus status;

    @Size(max = 100, message = "Search name is too long")
    private String name;

    private Long from;
    private Long to;

    private String username;
}