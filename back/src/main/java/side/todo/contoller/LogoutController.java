package side.todo.contoller;

import io.jsonwebtoken.Jwt;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<?> logout(@CookieValue(name = "Refresh-Token", required = false)  String refreshToken,
                                    HttpServletResponse response) {
        if(refreshToken != null) {
            redisRefreshTokenRepository.delete(JwtUtil.getMemberId(refreshToken, jwtProperties));
        }

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
