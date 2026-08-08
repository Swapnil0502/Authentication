package com.swapnil.authentication.controller;

import com.swapnil.authentication.configs.security.JwtUtil;
import com.swapnil.authentication.dto.AuthenticationRequest;
import com.swapnil.authentication.dto.AuthenticationResponse;
import com.swapnil.authentication.dto.RegisterRequest;
import com.swapnil.authentication.dto.RegisterResponse;
import com.swapnil.authentication.entity.User;
import com.swapnil.authentication.enums.Role;
import com.swapnil.authentication.service.RegisterUserService;
import com.swapnil.authentication.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final RegisterUserService registerUserService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest req) {
        try {
            User user = registerUserService.registerUser(req.getEmail(), req.getPassword());
            RegisterResponse res = new RegisterResponse("user created succesully", user.getEmail(), user.getId(), user.getPassword(), user.getRole());
            return new ResponseEntity<>(res, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new RegisterResponse(e.getMessage(),null, null, null, null), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest req){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));

        UserDetails userDetails = userService.loadUserByUsername(req.getEmail());

        Set<Role> roles = userDetails.getAuthorities().stream().map(auth -> Role.valueOf(auth.getAuthority())).collect(Collectors.toSet());

        String token = jwtUtil.generateToken(
                userDetails.getUsername(),
                roles
        );
        return new ResponseEntity<>(new AuthenticationResponse(token), HttpStatus.OK);
    }
}
