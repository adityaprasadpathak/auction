package com.eauction.www.auction;

import com.eauction.www.auction.models.UserRegistration;
import com.google.gson.Gson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class AuctionApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuctionApplication.class, args);
        /*
         * UserRegistration ur = UserRegistration.builder() .address("Bangalore") .contactNumber("9876543210")
         * .emailId("adi@gmail.com") .firstName("Aditya") .lastname("Pathak") .isAdmin(true) .userName("apathak")
         * .password("root123") .build(); System.out.println(new Gson().toJson(ur));
         */

    }

}
