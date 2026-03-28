package com.eauction.www.auction.dto;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class UserEntity {

    @Id
    @GeneratedValue
    private Integer id;

    private boolean active;

    @Column(unique = true)
    private String username;

    private String password;

    private String roles;

    private String firstname;
    private String middlename;
    private String lastname;

    @Column(unique = true)
    private String contactNumber;
    private String address;

    @Column(unique = true)
    private String emailId;

    private String orgType;
    private Long createdTime;
    private Long modifiedTime;
    private String createdBy;

    private boolean isBlacklisted;


}
