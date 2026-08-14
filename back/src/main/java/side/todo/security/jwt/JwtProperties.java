package side.todo.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "spring.jwt")
public class JwtProperties {
    private String prefix;
    private String secret;
    private Long accessExpirationTime;
    private Long refreshExpirationTime;
    private String accessTokenHeader;
    private String refreshTokenHeader;
}
