package com.mycompany.securetaskmanager.security;

import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptService {

    private final int MAX_ATTEMPT = 5;
    private final long LOCK_TIME_DURATION = 5 * 60 * 1000; // 5 минут

    private Map<String, AttemptInfo> attemptsCache = new ConcurrentHashMap<>();

    private static class AttemptInfo {
        int attempts;
        long lockTime; // время начала блокировки (в миллисекундах)
    }

    public void loginSucceeded(String username) {
        attemptsCache.remove(username);
    }

    public void loginFailed(String username) {
        AttemptInfo info = attemptsCache.getOrDefault(username, new AttemptInfo());
        info.attempts++;
        if (info.attempts >= MAX_ATTEMPT) {
            info.lockTime = Instant.now().toEpochMilli();
        }
        attemptsCache.put(username, info);
    }

    public boolean isBlocked(String username) {
        AttemptInfo info = attemptsCache.get(username);
        if (info == null) {
            return false;
        }
        if (info.attempts < MAX_ATTEMPT) {
            return false;
        }
        long now = Instant.now().toEpochMilli();
        if (now - info.lockTime > LOCK_TIME_DURATION) {
            attemptsCache.remove(username);
            return false;
        }
        return true;
    }

    public long getRemainingLockTime(String username) {
        AttemptInfo info = attemptsCache.get(username);
        if (info == null) return 0;
        long now = Instant.now().toEpochMilli();
        long elapsed = now - info.lockTime;
        return elapsed > LOCK_TIME_DURATION ? 0 : (LOCK_TIME_DURATION - elapsed);
    }
}
