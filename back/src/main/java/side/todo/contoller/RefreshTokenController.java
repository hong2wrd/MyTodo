package side.todo.contoller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import side.todo.domain.Member;
import side.todo.exception.ApiException;
import side.todo.exception.ErrorCode;
import side.todo.repository.redis.RedisRefreshTokenRepository;
import side.todo.security.MemberDetails;
import side.todo.security.jwt.JwtProperties;
import side.todo.security.jwt.JwtType;
import side.todo.security.jwt.JwtUtil;

@Slf4j
@RestController
@RequestMapping("/auth/refresh")
@RequiredArgsConstructor
public class RefreshTokenController {

    private final RedisRefreshTokenRepository redisRefreshTokenRepository;
    private final JwtProperties jwtProperties;

    @PostMapping
    public ResponseEntity<?> refresh(@CookieValue(name = "Refresh-Token") String refreshToken) {
        String memberId = JwtUtil.getMemberId(refreshToken, jwtProperties);

        String findRefreshToken = redisRefreshTokenRepository.findByMemberId(memberId)
                .orElseThrow(() -> new ApiException(ErrorCode.BAD_REQUEST));

        // Cookie와 Redis의 Refresh Token이 다를 경우
        if(!refreshToken.equals(findRefreshToken)) {
            throw new ApiException(ErrorCode.BAD_REQUEST);
        }

        try {
            MemberDetails memberDetails = (MemberDetails) JwtUtil.verify(refreshToken, jwtProperties);
            Member member = memberDetails.getMember();
            // Access Token 생성
            String newAccessToken = JwtUtil.create(member, jwtProperties, JwtType.ACCESS);

            return ResponseEntity.ok(newAccessToken);
        } catch (RuntimeException e) {
            log.error(e.toString());
            throw new ApiException(ErrorCode.BAD_REQUEST);
        }
    }
}
