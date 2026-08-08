package com.swapnil.authentication.service;

import com.swapnil.authentication.entity.User;
import com.swapnil.authentication.enums.Role;
import com.swapnil.authentication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class RegisterUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(String email, String password){
        if(userRepository.existsByEmail(email)){
            throw new RuntimeException("User already exists");
        }
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setRole(Set.of(Role.USER));

        return userRepository.save(newUser);
    }
}
