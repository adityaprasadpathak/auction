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
public class StopAuctionScheduler {

    private final AuctionRepository auctionRepository;

    /**
     * Scheduler to stop auctions whose end time has passed.
     *
     * Runs every 20 minutes.
     */
    @Scheduled(cron = "0 */20 * * * ?")
    public void stopExpiredAuctions() {

        System.out.println("StopAuctionScheduler Executed at " + Date.from(Instant.now()));

        long currentTime = Instant.now().toEpochMilli();

        // Fetch only active auctions which are expired
        List<AuctionEntity> expiredAuctions =
                auctionRepository.findByStatusAndStopTimestampLessThan(
                        AuctionStatus.IN_PROGRESS,
                        currentTime
                );

        if (expiredAuctions.isEmpty()) {
            return;
        }

        // Update status
        expiredAuctions.forEach(auction ->
                auction.setStatus(AuctionStatus.FINISHED)
        );

        // Bulk save
        auctionRepository.saveAll(expiredAuctions);
    }
}