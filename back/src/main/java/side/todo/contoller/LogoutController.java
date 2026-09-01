package side.todo.contoller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import side.todo.repository.redis.RedisRefreshTokenRepository;
import side.todo.security.jwt.JwtProperties;
import side.todo.security.jwt.JwtUtil;

import static side.todo.util.ResponseUtil.*;

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

        removeCookie(response, jwtProperties, secure);

        return ResponseEntity.ok().build();
    }
}
