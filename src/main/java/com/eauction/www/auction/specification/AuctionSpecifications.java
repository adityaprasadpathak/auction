package com.eauction.www.auction.specification;

import com.eauction.www.auction.dto.AuctionEntity;
import com.eauction.www.auction.models.AuctionStatus;
import org.springframework.data.jpa.domain.Specification;

public class AuctionSpecifications {
    private AuctionSpecifications() {
        /* This utility class should not be instantiated */
    }

    public static Specification<AuctionEntity> hasStatus(AuctionStatus status) {
        return (root, query, cb) -> status == null ? cb.conjunction() : cb.equal(root.get("status"), status);
    }

    public static Specification<AuctionEntity> nameContains(String name) {
        return (root, query, cb) -> name == null ? cb.conjunction() : cb.like(cb.lower(root.get("auctionName")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<AuctionEntity> startsAfter(Long from) {
        return (root, query, cb) -> from == null ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("startTimestamp"), from);
    }

    public static Specification<AuctionEntity> startsBefore(Long to) {
        return (root, query, cb) -> to == null ? cb.conjunction() : cb.lessThanOrEqualTo(root.get("startTimestamp"), to);
    }

    public static Specification<AuctionEntity> usernameEquals(String username) {
        return (root, query, cb) -> username == null ? cb.conjunction() : cb.equal(root.get("username"), username);
    }
}