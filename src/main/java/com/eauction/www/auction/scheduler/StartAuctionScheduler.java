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
public class StartAuctionScheduler {

    private final AuctionRepository auctionRepository;

    @Transactional
    @Scheduled(fixedDelay = 30000)
    public synchronized void startScheduledAuctions() {
        long now = Instant.now().toEpochMilli();

        int updated = auctionRepository.startEligibleAuctions(now);

        log.info("Started {} auctions", updated);
    }
}