package com.eauction.www.auction.search.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSearchCriteria {
    @Min(1)
    private Integer limit = 10;
    @PositiveOrZero
    private Integer offset = 0;
    
    private String username;
    private String firstname;
    private String emailId;
    private String orgType;
    private Boolean active;
    private Boolean isBlacklisted;
}