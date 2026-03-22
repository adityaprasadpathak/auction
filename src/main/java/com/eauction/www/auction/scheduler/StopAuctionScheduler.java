package com.eauction.www.auction.scheduler;

import com.eauction.www.auction.dto.AuctionEntity;
import com.eauction.www.auction.models.AuctionStatus;
import com.eauction.www.auction.repository.AuctionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class StopAuctionScheduler {

    private final AuctionRepository auctionRepository;

    /**
     * Scheduler to stop auctions whose end time has passed.
     *
     * Runs every 20 minutes.
     */
    //@Scheduled(cron = "0 */20 * * * ?")
    @Scheduled(fixedDelay = 30000)
    @Transactional
    public synchronized void stopExpiredAuctions() {

        long now = Instant.now().toEpochMilli();

        int updated = auctionRepository.stopExpiredAuctions(now);

        log.info("Stopped {} auctions", updated);
    }
}