package berkut.abc.telegram_service.config.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class RateLimitingService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String RATE_LIMIT_PREFIX = "rate_limit:";
    private static final int MAX_ATTEMPTS = 5;
    private static final long WINDOW_SIZE_MINUTES = 15;

    public boolean isAllowed(String identifier) {
        String key = RATE_LIMIT_PREFIX + identifier;

        try {
            String attempts = redisTemplate.opsForValue().get(key);

            if (attempts == null) {
                redisTemplate.opsForValue().set(key, "1", Duration.ofMinutes(WINDOW_SIZE_MINUTES));
                return true;
            }

            int currentAttempts = Integer.parseInt(attempts);
            if (currentAttempts >= MAX_ATTEMPTS) {
                log.warn("Rate limit exceeded for identifier: {}", identifier);
                return false;
            }

            redisTemplate.opsForValue().increment(key);
            return true;

        } catch (Exception e) {
            log.error("Error checking rate limit: {}", e.getMessage());
            return true;
        }
    }

    public void reset(String identifier) {
        String key = RATE_LIMIT_PREFIX + identifier;
        redisTemplate.delete(key);
    }
}