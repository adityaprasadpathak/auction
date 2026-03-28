package com.eauction.www.auction.specification;

import com.eauction.www.auction.dto.UserEntity;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {


    public static Specification<UserEntity> hasUsername(String username) {
        return (root, query, cb) -> (username == null || username.isEmpty()) 
            ? cb.conjunction() : cb.like(cb.lower(root.get("username")), "%" + username.toLowerCase() + "%");
    }

    public static Specification<UserEntity> hasEmail(String email) {
        return (root, query, cb) -> (email == null || email.isEmpty()) 
            ? cb.conjunction() : cb.equal(root.get("emailId"), email);
    }

    public static Specification<UserEntity> isType(String orgType) {
        return (root, query, cb) -> orgType == null ? cb.conjunction() : cb.equal(root.get("orgType"), orgType);
    }

    public static Specification<UserEntity> isActive(Boolean active) {
        return (root, query, cb) -> active == null ? cb.conjunction() : cb.equal(root.get("active"), active);
    }

    public static Specification<UserEntity> hasFirstname(String firstname) {
        return (root, query, cb) -> (firstname == null || firstname.isEmpty())
                ? cb.conjunction() : cb.like(cb.lower(root.get("username")), "%" + firstname.toLowerCase() + "%");
    }
}