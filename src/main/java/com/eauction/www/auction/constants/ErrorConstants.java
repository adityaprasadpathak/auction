package com.eauction.www.auction.constants;

import com.eauction.www.auction.models.ServiceErrorCode;

public final class ErrorConstants {

    public final static String WRONG_AUCTION_TIMESTAMP = "Please check start and stop time of the auction";
    public final static String OWNER_BID_NOT_ALLOWED = "Owner is not allowed to bid in his own Auction";

    public static final String BID_LESS_CURR_VAL = "Requested Bid is less than the current value";
    public static final String AUCTION_NOT_ACTIVE = "Auction is not Active";

    public static final String ITEM_DOESNT_BELONG_TO_AUCTION = "Item doesn't belong to the Auction";

     private ErrorConstants() {
         // private constructor to prevent instantiation
     }




}
