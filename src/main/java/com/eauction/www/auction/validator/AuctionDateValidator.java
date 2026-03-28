package com.eauction.www.auction.validator;

import com.eauction.www.auction.models.Auction;
import com.eauction.www.auction.validator.annotation.ValidAuctionDuration;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AuctionDateValidator implements ConstraintValidator<ValidAuctionDuration, Auction> {

    private static final long ONE_DAY_MS = 24 * 60 * 60 * 1000L;
    private static final long SEVEN_DAYS_MS = 7 * ONE_DAY_MS;

    @Override
    public boolean isValid(Auction auction, ConstraintValidatorContext context) {
        // Let @NotNull handle initial null checks
        if (auction.getStartTimestamp() == null || auction.getStopTimestamp() == null) {
            return true;
        }

        long duration = auction.getStopTimestamp() - auction.getStartTimestamp();

        // 1. Check if End is before Start
        if (duration <= 0) {
            setCustomMessage(context, "Stop time must be strictly after start time", "stopTimestamp");
            return false;
        }

        // 2. Check Min Duration (1 Day)
        if (duration < ONE_DAY_MS) {
            setCustomMessage(context, "Auction must last at least 24 hours", "stopTimestamp");
            return false;
        }

        // 3. Check Max Duration (7 Days)
        if (duration > SEVEN_DAYS_MS) {
            setCustomMessage(context, "Auction cannot last longer than 7 days", "stopTimestamp");
            return false;
        }

        return true;
    }

    private void setCustomMessage(ConstraintValidatorContext context, String message, String field) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(field)
                .addConstraintViolation();
    }
}