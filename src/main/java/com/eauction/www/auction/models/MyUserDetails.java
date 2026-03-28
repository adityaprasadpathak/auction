package com.eauction.www.auction.models;

import com.eauction.www.auction.dto.UserEntity;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@NoArgsConstructor
public class MyUserDetails implements UserDetails {

    private String password;
    private String username;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private List<GrantedAuthority> authorities;

    public MyUserDetails(UserEntity userEntity) {
        this.setAccountNonExpired(userEntity.isActive());
        this.setAccountNonLocked(userEntity.isActive());
        this.setCredentialsNonExpired(userEntity.isActive());
        this.setEnabled(userEntity.isActive());
        this.setPassword(userEntity.getPassword());
        this.setUsername(userEntity.getUsername());
        this.setAuthorities(Arrays.stream(userEntity.getRoles().split(",")).map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
