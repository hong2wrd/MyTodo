package side.todo.repository.redis;

import java.util.Optional;

public interface RefreshTokenRepository {

    void save(String memberId, String refreshToken, long ttlSeconds);

    Optional<String> findByMemberId(String memberId);

    void delete(String memberId);
}
