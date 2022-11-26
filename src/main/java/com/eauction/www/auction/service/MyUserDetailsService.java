package com.eauction.www.auction.service;

import com.eauction.www.auction.dto.UserEntity;
import com.eauction.www.auction.models.MyUserDetails;
import com.eauction.www.auction.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(s);
        return convertUserEntity(userEntity);
    }

    private MyUserDetails convertUserEntity(UserEntity userEntity) {

        MyUserDetails myUserDetails = new MyUserDetails();
        myUserDetails.setAccountNonExpired(userEntity.isActive());
        ;
        myUserDetails.setAccountNonLocked(userEntity.isActive());
        myUserDetails.setCredentialsNonExpired(userEntity.isActive());
        myUserDetails.setEnabled(userEntity.isActive());
        myUserDetails.setPassword(userEntity.getPassword());
        myUserDetails.setUsername(userEntity.getUsername());
        myUserDetails.setAuthorities(Arrays.stream(userEntity.getRoles().split(",")).map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList()));
        return myUserDetails;
    }
}
