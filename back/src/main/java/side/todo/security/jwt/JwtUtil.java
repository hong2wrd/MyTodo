package side.todo.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import side.todo.domain.Member;
import side.todo.security.MemberDetails;

import java.util.Date;

public class JwtUtil {

    /**
     * 토큰 생성
     * return AccessToken or RefreshToken
     */
    public static String create(Member member, JwtProperties jwtProperties, JwtType jwtType) {
        String prefix = JwtType.ACCESS.equals(jwtType) ? jwtProperties.getPrefix() : "";
        Long expirationTime = JwtType.ACCESS.equals(jwtType) ? jwtProperties.getAccessExpirationTime() : jwtProperties.getRefreshExpirationTime();

        String jwtToken = JWT.create()
                .withSubject("MY_TODO")
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .withClaim("id", member.getMemberId())
                .withClaim("name", member.getName())
                .withClaim("role", member.getRole().name())
                .withClaim("type", jwtType.toString())
                .sign(Algorithm.HMAC512(jwtProperties.getSecret()));
        return prefix + jwtToken;
    }

    /**
     * 토큰 확인
     * return MemberDetails
     */
    public static UserDetails verity(String token, JwtProperties jwtProperties) {
        try {
            DecodedJWT decodedJwt = JWT
                    .require(Algorithm.HMAC512(jwtProperties.getSecret()))
                    .build()
                    .verify(token.replace(jwtProperties.getPrefix(), ""));
            String id = decodedJwt.getClaim("id").asString();
            String role = decodedJwt.getClaim("role").asString();
            String name = decodedJwt.getClaim("name").asString();
            return new MemberDetails(Member.of(id, role, name));
        } catch(JWTVerificationException e) {
            throw new InternalAuthenticationServiceException( "JWT가 만료되었거나 유효하지 않습니다.", e);
        }
    }

    /**
     * 토큰에서 사용자 아이디 추출
     * return memberId
     */
    public static String getMemberId(String token, JwtProperties jwtProperties) {
        DecodedJWT decodedJwt = JWT
                .require(Algorithm.HMAC512(jwtProperties.getSecret()))
                .build()
                .verify(token.replace(jwtProperties.getPrefix(), ""));
        return decodedJwt.getClaim("id").asString();
    }

}
