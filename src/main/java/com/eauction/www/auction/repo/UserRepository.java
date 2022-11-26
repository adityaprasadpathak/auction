package com.eauction.www.auction.repo;

import com.eauction.www.auction.dto.UserEntity;
import com.eauction.www.auction.models.MyUserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer> {

    public UserEntity findByUsername(String username);

}
