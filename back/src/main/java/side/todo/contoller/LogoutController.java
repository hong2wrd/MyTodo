package side.todo.contoller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import side.todo.repository.redis.RedisRefreshTokenRepository;
import side.todo.security.jwt.JwtProperties;
import side.todo.security.jwt.JwtUtil;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/logout")
public class LogoutController {

    private final RedisRefreshTokenRepository redisRefreshTokenRepository;
    private final JwtProperties jwtProperties;

    @Value("${cookie.secure}")
    private boolean secure;

    @PostMapping
    public ResponseEntity<?> logout(@RequestHeader(value = "Authorization") String accessToken,
                                    HttpServletResponse response) {

        redisRefreshTokenRepository.delete(JwtUtil.getMemberId(accessToken, jwtProperties));

        ResponseCookie cookie = ResponseCookie
                .from(jwtProperties.getRefreshTokenHeader(), "")
                .httpOnly(secure)
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ZERO)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok().build();
    }
}
