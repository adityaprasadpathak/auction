package com.eauction.www.auction.repository;

import com.eauction.www.auction.dto.BidEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BidRepository extends JpaRepository<BidEntity, String> {
    // You can add custom query methods here if needed
    @Query("SELECT b FROM BidEntity b WHERE b.itemId = :itemId AND b.auctionId = :auctionId AND b.bid = (SELECT MAX(b2.bid) FROM BidEntity b2 WHERE b2.itemId = :itemId AND b2.auctionId = :auctionId)")
    Optional<BidEntity> findHighestBidByItemIdAndAuctionId(@Param("itemId") String itemId, @Param("auctionId") String auctionId);

    List<BidEntity> findTop3ByAuctionIdAndItemIdOrderByBidDesc(String auctionId, String itemId);



}
