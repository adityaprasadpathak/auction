package com.eauction.www.auction.repository;

import com.eauction.www.auction.dto.BidEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BidRepository extends JpaRepository<BidEntity, String> {

    /**
     * Finds the highest bid for an item and locks the row.
     * Use PageRequest.of(0, 1) to get only the top result.
     * * @Lock(LockModeType.PESSIMISTIC_WRITE) ensures that if two users bid
     * at the same time, the database forces them to queue up.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM BidEntity b WHERE b.itemId = :itemId AND b.auctionId = :auctionId ORDER BY b.bid DESC")
    List<BidEntity> findTopBidWithLock(@Param("itemId") String itemId,
                                       @Param("auctionId") String auctionId,
                                       Pageable pageable);

    /**
     * Non-locking version for read-only views (e.g., displaying current price on a dashboard).
     */
    @Query("SELECT b FROM BidEntity b WHERE b.itemId = :itemId AND b.auctionId = :auctionId ORDER BY b.bid DESC")
    List<BidEntity> findTopBids(@Param("itemId") String itemId,
                                @Param("auctionId") String auctionId,
                                Pageable pageable);

    /**
     * Find all bids for an item, sorted by highest first.
     */
    List<BidEntity> findByAuctionIdAndItemIdOrderByBidDesc(String auctionId, String itemId);

    /**
     * Count total bids placed on an item.
     */
    long countByAuctionIdAndItemId(String auctionId, String itemId);

    /**
     * Check if a user has already placed a bid in this auction.
     */
    boolean existsByAuctionIdAndUsername(String auctionId, String username);

    @Query("SELECT b FROM BidEntity b WHERE b.itemId = :itemId AND b.auctionId = :auctionId AND b.bid = (SELECT MAX(b2.bid) FROM BidEntity b2 WHERE b2.itemId = :itemId AND b2.auctionId = :auctionId)")
    Optional<BidEntity> findHighestBidByItemIdAndAuctionId(String itemId, String auctionId);

    List<BidEntity> findBidsByItemIdAndAuctionIdAndUsername(String itemId, String auctionId, String username);

    List<BidEntity> findBidsByItemIdAndAuctionId(String itemId, String auctionId);

    @Query("""
        SELECT b FROM BidEntity b 
        JOIN UserEntity u ON b.username = u.username 
        WHERE b.itemId = :itemId 
          AND b.auctionId = :auctionId 
          AND u.isBlacklisted = false 
        ORDER BY b.bid DESC, b.bidTime ASC
    """)
    List<BidEntity> findTopValidBid(@Param("itemId") String itemId,
                                    @Param("auctionId") String auctionId,
                                    Pageable pageable);

    default Optional<BidEntity> findWinningBid(String auctionId, String itemId) {
        List<BidEntity> results = findTopValidBid(itemId, auctionId, PageRequest.of(0, 1));
        return results.stream().findFirst();
    }


}