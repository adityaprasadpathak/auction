package com.eauction.www.auction.controller;

import com.eauction.www.auction.service.AuctionService;
import com.eauction.www.auction.service.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admins/metrics")
public class AdminMetricController {


    private final MyUserDetailsService myUserDetailsService;
    private final AuctionService auctionService;


    @GetMapping
    public Map<String, Number> getMetrics() {
        int totalUsers = myUserDetailsService.getTotalUsers();
        int totalLiveAuction = auctionService.getLiveAuctions();
        int totalAwaitingApprovalAuctions = auctionService.getTotalAwaitingApprovalAuctions();
        double totalVolume = auctionService.getTotalVolume();

        return Map.of(
                "totalUsers", totalUsers,
                "totalLiveAuction", totalLiveAuction,
                "totalAwaitingApprovalAuctions", totalAwaitingApprovalAuctions,
                "totalVolume", totalVolume
        );
    }
}
