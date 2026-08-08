package com.swapnil.authentication.controller;

import com.swapnil.authentication.entity.User;
import com.swapnil.authentication.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile() {
          Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

          String email = authentication.getName();
            log.info("fetching user profile for user : {}" , email);
          User user = userService.findByEmail(email);
        log.info("user profile for fetched successfully for user : {}" , email);
        return ResponseEntity.ok(user);
    }
}
