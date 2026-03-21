package com.eauction.www.auction.request.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthenticateRequest {

    private String username;
    private String password;

    public AuthenticateRequest() {
    }

    public AuthenticateRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
