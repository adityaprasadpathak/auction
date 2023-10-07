package com.eauction.www.auction.scheduler;


import com.eauction.www.auction.task.StopAuction;
import com.eauction.www.auction.service.AuctionService;
import com.eauction.www.auction.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component
public class StopAuctionScheduler {

    @Autowired
    AuctionService auctionService;
    @Autowired
    ThreadPoolTaskScheduler taskScheduler;

    //@Scheduled(cron = "0 58 23 * * ?") // Cron expression for 11:59:00 PM every day
    @Scheduled(cron = "0 25 15 * * ?")
    public void runDailyTask() {
        System.out.println("StopAuctionScheduler Executed at " + Date.from(Instant.now()));
        auctionService.getAuctions(Utility.getTimestampsForTomorrowMidnight(), Utility.getTimestampsForTomorrowEOD())
                .parallelStream().
                forEach(auction -> taskScheduler.schedule(new StopAuction(auction,auctionService), Date.from(Instant.ofEpochMilli(auction.getStartTimestamp()))));
    }
}
