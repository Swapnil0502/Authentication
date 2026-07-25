package com.swapnil.authentication.repository;

import com.swapnil.authentication.dto.RegisterRequest;
import com.swapnil.authentication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
Optional<User> findByEmail(String email);
Boolean existsByEmail(String email);
}
