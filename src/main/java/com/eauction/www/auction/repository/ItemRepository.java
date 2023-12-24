package com.eauction.www.auction.repository;

import com.eauction.www.auction.dto.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<ItemEntity, String> {
    // You can define additional query methods here if needed
}
