package com.eauction.www.auction.scheduler;

import com.eauction.www.auction.dto.AuctionEntity;
import com.eauction.www.auction.models.AuctionStatus;
import com.eauction.www.auction.repository.AuctionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StartAuctionScheduler {

    private final AuctionRepository auctionRepository;

    /**
     * Scheduler to start auctions whose start time has arrived.
     *
     * Runs every 5 minutes.
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void startScheduledAuctions() {

        System.out.println("StartAuctionScheduler Executed at " + Date.from(Instant.now()));

        long currentTime = Instant.now().toEpochMilli();

        // Fetch only auctions ready to start
        List<AuctionEntity> auctionsToStart =
                auctionRepository.findByStatusIsNullAndStartTimestampLessThanEqual(currentTime);

        if (auctionsToStart.isEmpty()) {
            return;
        }

        // Update status
        auctionsToStart.forEach(auction ->
                auction.setStatus(AuctionStatus.IN_PROGRESS)
        );

        // Bulk save
        auctionRepository.saveAll(auctionsToStart);
    }
}