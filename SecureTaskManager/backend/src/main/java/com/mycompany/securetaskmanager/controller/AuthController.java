package com.mycompany.securetaskmanager.controller;

import com.mycompany.securetaskmanager.audit.AuditService;
import com.mycompany.securetaskmanager.model.RefreshToken;
import com.mycompany.securetaskmanager.model.Role;
import com.mycompany.securetaskmanager.model.User;
import com.mycompany.securetaskmanager.repository.RoleRepository;
import com.mycompany.securetaskmanager.repository.UserRepository;
import com.mycompany.securetaskmanager.security.JwtTokenProvider;
import com.mycompany.securetaskmanager.security.LoginAttemptService;
import com.mycompany.securetaskmanager.service.RefreshTokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private AuditService auditService;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            auditService.log("Failed registration: username is taken -  " + user.getUsername());
            return ResponseEntity.badRequest().body(Map.of("message", "Username is already taken"));
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByName("ROLE_USER");
        user.setRoles(Collections.singleton(userRole));
        userRepository.save(user);

        auditService.log("Successful registration of new user: " + user.getUsername());

        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");

        // Проверяем блокировку по количеству неудачных попыток
        if (loginAttemptService.isBlocked(username)) {
            long remainingMs = loginAttemptService.getRemainingLockTime(username);
            String message = String.format("Account is locked. Please try again in %d seconds..", remainingMs / 1000);
            auditService.log("Account locked due to too many failed login attempts: " + username);
            return ResponseEntity.status(429).body(Map.of("message", message));
        }

        String password = loginRequest.get("password");

        return userRepository.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .map(user -> {
                    // Успешный вход — сбрасываем счетчик попыток
                    loginAttemptService.loginSucceeded(username);

                    String accessToken = jwtTokenProvider.createToken(username);
                    RefreshToken refreshToken = refreshTokenService.createRefreshToken(username);

                    auditService.log("Successful user login: " + username);

                    return ResponseEntity.ok(Map.of(
                            "accessToken", accessToken,
                            "refreshToken", refreshToken.getToken()
                    ));
                })
                .orElseGet(() -> {
                    // Неудачный вход — увеличиваем счетчик попыток
                    loginAttemptService.loginFailed(username);

                    auditService.log("Failed login attempt: " + username);

                    return ResponseEntity.status(401).body(Map.of("message", "Invalid credentials"));
                });
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        String requestToken = request.get("refreshToken");

        return refreshTokenService.findByToken(requestToken)
                .filter(token -> !refreshTokenService.isExpired(token))
                .map(token -> {
                    String newAccessToken = jwtTokenProvider.createToken(token.getUser().getUsername());

                    auditService.log("Access token updated for user: " + token.getUser().getUsername());

                    return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
                })
                .orElseGet(() -> {
                    auditService.log("Failed token refresh attempt (refresh token is invalid or expired)");
                    return ResponseEntity.status(403).body(Map.of("message", "Invalid or expired refresh token"));
                });
    }
}
