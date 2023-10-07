package com.eauction.www.auction.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilityTest {

    @Test
    void calculateCronExpression() {

        Long timestamp = 1696630809000L;
        String cronExp = Utility.calculateCronExpression(timestamp);
        System.out.println(cronExp);
    }
}