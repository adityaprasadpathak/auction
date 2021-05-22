package com.eauction.www.auction.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import java.util.Collection;

/**
 * Request Context is a service, contains information about User(i.e username and whether it is Admin or not).
 * Request Context is populated every time a call is made.
 * This is done in JwtFilter.class, during JWT token validation.
 * For Open API (Request Context will be Empty or default-value)
 */
@Service
public class RequestContext {


 private String username;
 private boolean isAdmin;


    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
