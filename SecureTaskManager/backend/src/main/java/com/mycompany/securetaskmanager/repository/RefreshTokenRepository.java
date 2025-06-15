package com.mycompany.securetaskmanager.repository;

import com.mycompany.securetaskmanager.model.RefreshToken;
import com.mycompany.securetaskmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
}
