package com.eauction.www.auction.service;

import com.eauction.www.auction.dto.UserEntity;
import com.eauction.www.auction.exception.UserNotFoundException;
import com.eauction.www.auction.models.MyUserDetails;
import com.eauction.www.auction.models.ServiceErrorCode;
import com.eauction.www.auction.repository.UserRepository;
import com.eauction.www.auction.search.models.UserSearchCriteria;
import com.eauction.www.auction.specification.UserSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(s);
        return convertUserEntity(userEntity);
    }

    private MyUserDetails convertUserEntity(UserEntity userEntity) {

        MyUserDetails myUserDetails = new MyUserDetails();
        myUserDetails.setAccountNonExpired(userEntity.isActive());
        myUserDetails.setAccountNonLocked(userEntity.isActive());
        myUserDetails.setCredentialsNonExpired(userEntity.isActive());
        myUserDetails.setEnabled(userEntity.isActive());
        myUserDetails.setPassword(userEntity.getPassword());
        myUserDetails.setUsername(userEntity.getUsername());
        myUserDetails.setAuthorities(Arrays.stream(userEntity.getRoles().split(",")).map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList()));
        return myUserDetails;
    }

    public List<MyUserDetails> getAllActiveUsers() {
        List<UserEntity> userEntities = userRepository.findByActive(true);
        return userEntities.stream().map(this::convertUserEntity).toList();
    }

    public Page<MyUserDetails> getAllUsers(UserSearchCriteria criteria) {
        // Calculate page number (Spring uses 0-based indexing)
        int pageNumber = criteria.getOffset() / criteria.getLimit();
        Pageable pageable = PageRequest.of(pageNumber, criteria.getLimit(), Sort.by("createdTime").descending());

        // Combine filters
        Specification<UserEntity> spec = Specification.allOf(
                UserSpecifications.hasUsername(criteria.getUsername()),
                UserSpecifications.hasEmail(criteria.getEmailId()),
                UserSpecifications.isType(criteria.getOrgType()),
                UserSpecifications.isActive(criteria.getActive()),
                UserSpecifications.hasFirstname(criteria.getFirstname())
        );

        // Fetch and map to your details model
        return userRepository.findAll(spec, pageable)
                .map(MyUserDetails::new);
    }

    public void blacklistUser(String username, boolean blacklist) {
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity != null) {
            userEntity.setBlacklisted(blacklist);
            userRepository.save(userEntity);
        } else {
            throw new UserNotFoundException("User not found with username: " + username, ServiceErrorCode.USER_NOT_FOUND);
        }
    }

    public int getTotalUsers() {
        return (int) userRepository.count();
    }
}
