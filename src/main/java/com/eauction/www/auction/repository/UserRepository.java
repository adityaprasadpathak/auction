package com.eauction.www.auction.repository;

import com.eauction.www.auction.dto.AuctionEntity;
import com.eauction.www.auction.dto.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer> , JpaSpecificationExecutor<UserEntity> {

    public UserEntity findByUsername(String username);

    List<UserEntity> findByActive(boolean b);
}
