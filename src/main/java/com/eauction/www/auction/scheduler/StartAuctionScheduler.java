package com.eauction.www.auction.scheduler;

import com.eauction.www.auction.task.StartAuction;
import com.eauction.www.auction.service.AuctionService;
import com.eauction.www.auction.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component
public class StartAuctionScheduler {

    @Autowired
    AuctionService auctionService;
    @Autowired
    ThreadPoolTaskScheduler taskScheduler;

    //@Scheduled(cron = "0 59 23 * * ?") // Cron expression for 11:59:00 PM every day
    @Scheduled(cron = "0 9 17 * * ?")
    public void runDailyTask() {
        System.out.println("StartAuctionScheduler Executed at " + Date.from(Instant.now()));
        auctionService.getAuctions(Utility.getTimestampsForTomorrowMidnight(), Utility.getTimestampsForTomorrowEOD())
                .parallelStream().
                forEach(auction -> taskScheduler.schedule(new StartAuction(auction,auctionService), Date.from(Instant.ofEpochMilli(auction.getStartTimestamp()))));
    }
}
