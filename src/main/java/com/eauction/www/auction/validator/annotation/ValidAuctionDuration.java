package com.eauction.www.auction.validator.annotation;

import com.eauction.www.auction.validator.AuctionDateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AuctionDateValidator.class)
public @interface ValidAuctionDuration {
    String message() default "Auction duration must be between 1 second and 7 days";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}