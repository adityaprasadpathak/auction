package com.eauction.www.auction.repository;

import com.eauction.www.auction.dto.AuctionEntity;
import com.eauction.www.auction.models.AuctionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AuctionRepository extends JpaRepository<AuctionEntity, String> {
    // You can define additional query methods here if needed
    Optional<AuctionEntity> findByAuctionId(String auctionId);

    List<AuctionEntity> findByUsername(String username);

    Optional<AuctionEntity> findByAuctionIdAndUsername(String auctionId, String username);

    @Query("SELECT a FROM AuctionEntity a WHERE a.startTimestamp BETWEEN :currentTime AND :startTime")
    List<AuctionEntity> findAuctionsBetweenStartTimestamp(@Param("currentTime") Long currentTime, @Param("startTime") Long startTime);

    List<AuctionEntity> findByStatusAndStopTimestampLessThan(AuctionStatus status, long timestamp);

    List<AuctionEntity> findByStatusIsNullAndStartTimestampLessThanEqual(long timestamp);

    List<AuctionEntity> findByStatus(AuctionStatus auctionStatus);

    @Modifying
    @Query("""
    UPDATE AuctionEntity a 
    SET a.status = 'FINISHED' 
    WHERE a.status = 'IN_PROGRESS' 
      AND a.stopTimestamp < :now
""")
    int stopExpiredAuctions(@Param("now") long now);
}
