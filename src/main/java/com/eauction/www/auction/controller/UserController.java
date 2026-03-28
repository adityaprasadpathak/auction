package com.eauction.www.auction.controller;

import com.eauction.www.auction.models.MyUserDetails;
import com.eauction.www.auction.search.models.UserSearchCriteria;
import com.eauction.www.auction.service.MyUserDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final MyUserDetailsService myUserDetailsService;


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<MyUserDetails>> getAllUsers(@Valid UserSearchCriteria criteria) {
        Page<MyUserDetails> users = myUserDetailsService.getAllUsers(criteria);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> blacklistUser(@PathVariable String username, @RequestParam boolean blacklist) {
        try {
            myUserDetailsService.blacklistUser(username, blacklist);
            String message = blacklist ? "User blacklisted successfully." : "User removed from blacklist successfully.";
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            log.error("Error blacklisting user: {}", e.getMessage());
            return ResponseEntity.status(500).body("An error occurred while updating the user's blacklist status.");
        }
    }
}
