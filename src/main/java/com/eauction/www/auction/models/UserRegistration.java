package com.eauction.www.auction.models;

import lombok.*;


@Builder
@Data
public class UserRegistration {

    private String userName;
    private String password;
    private String firstName;
    private String middleName;
    private String lastname;
    private String contactNumber;
    private String address;
    private String emailId;
    private String orgType;
    private boolean isAdmin;
    private String createdBy;
}
