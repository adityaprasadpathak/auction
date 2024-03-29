package com.eauction.www.auction.scheduler;

import com.eauction.www.auction.models.AuctionStatus;
import com.eauction.www.auction.repository.AuctionRepository;
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
    AuctionRepository auctionRepository;
    @Autowired
    ThreadPoolTaskScheduler taskScheduler;

    //@Scheduled(cron = "0 59 23 * * ?") // Cron expression for 11:59:00 PM every day
    @Scheduled(cron = "0 0/5 * * * ?") // Cron Run every 5 Minutes

    public void runDailyTask() {
        System.out.println("StartAuctionScheduler Executed at " + Date.from(Instant.now()));
/*        auctionService.getAuctions(Utility.getTimestampsForTomorrowMidnight(), Utility.getTimestampsForTomorrowEOD())
        auctionService.getAuctions()
                .parallelStream()
                .filter(auction -> auction.getStatus() == null)
                .forEach(auction -> auction.setStatus(AuctionStatus.IN_PROGRESS));*/

        auctionRepository.findAll()
                .parallelStream()
                .filter(auctionEntity -> auctionEntity.getStatus() == null)
                .forEach(auctionEntity -> {
                    auctionEntity.setStatus(AuctionStatus.IN_PROGRESS);
                    auctionRepository.save(auctionEntity);
                });
        //.forEach(auction -> taskScheduler.schedule(new StartAuction(auction,auctionService), Date.from(Instant.ofEpochMilli(auction.getStartTimestamp()))));
    }
}
