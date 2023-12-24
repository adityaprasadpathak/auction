package com.eauction.www.auction.repository;

import com.eauction.www.auction.dto.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer> {

    public UserEntity findByUsername(String username);

}
