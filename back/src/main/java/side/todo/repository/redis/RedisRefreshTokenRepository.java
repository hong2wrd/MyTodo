package side.todo.repository.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RedisRefreshTokenRepository implements RefreshTokenRepository {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String PREFIX = "refresh:";

    @Override
    public void save(String memberId, String refreshToken, long ttlSeconds) {
        redisTemplate.opsForValue()
                .set(PREFIX + memberId, refreshToken, ttlSeconds, TimeUnit.SECONDS);
    }

    @Override
    public Optional<String> findByMemberId(String memberId) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(PREFIX + memberId));
    }

    @Override
    public void delete(String memberId) {
        redisTemplate.delete(PREFIX +memberId);
    }
}
