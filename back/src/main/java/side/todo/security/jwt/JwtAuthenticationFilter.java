package side.todo.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import side.todo.dto.ResponseDto;
import side.todo.dto.login.LoginReqDto;
import side.todo.dto.login.LoginRespDto;
import side.todo.repository.redis.RedisRefreshTokenRepository;
import side.todo.security.MemberDetails;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.Duration;

import static side.todo.util.ResponseUtil.response;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtProperties jwtProperties;
    private final RedisRefreshTokenRepository redisRefreshTokenRepository;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   JwtProperties jwtProperties,
                                   RedisRefreshTokenRepository redisRefreshTokenRepository) {
        super(authenticationManager);
        setFilterProcessesUrl("/login"); // 로그인 URL 변경
        this.authenticationManager = authenticationManager;
        this.jwtProperties = jwtProperties;
        this.redisRefreshTokenRepository = redisRefreshTokenRepository;
    }

    /**
     * 로그인 요청 시 처리(POST)
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            ObjectMapper om = new ObjectMapper();
            LoginReqDto loginReqDto = om.readValue(request.getInputStream(), LoginReqDto.class);
            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(loginReqDto.getMemberId(), loginReqDto.getPassword());
            return authenticationManager.authenticate(authenticationToken);  // UserDetailService loadUserByUsername 호출
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException(e.getMessage());
        }
    }

    /**
     * 로그인 성공 시
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        MemberDetails memberDetails = (MemberDetails) authResult.getPrincipal();
        String jwtAccessToken = JwtUtil.create(memberDetails.getMember(), jwtProperties, JwtType.ACCESS);
        String jwtRefreshToken = redisRefreshTokenRepository.findByMemberId(memberDetails.getUsername())
                .orElseGet(() -> {
                    String newRefreshToken = JwtUtil.create(memberDetails.getMember(), jwtProperties, JwtType.REFRESH);
                    redisRefreshTokenRepository.save(memberDetails.getUsername(), newRefreshToken, jwtProperties.getRefreshExpirationTime());
                    return newRefreshToken;
                });

        response.addHeader(jwtProperties.getAccessTokenHeader(), jwtAccessToken);

        ResponseCookie cookie = ResponseCookie.from(jwtProperties.getRefreshTokenHeader(), jwtRefreshToken)
                .httpOnly(true)
                .secure(false) // 운영(HTTPS)에서는 true, 개발에서 false
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofDays(jwtProperties.getRefreshExpirationTime()))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        ResponseDto<LoginRespDto> responseDto = new ResponseDto<>(1, "로그인 성공", LoginRespDto.from(memberDetails.getMember()));

        response(response, responseDto, HttpStatus.OK);
    }

    /**
     * 로그인 실패 시
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response(response, new ResponseDto<>(-1, "로그인 실패", null), HttpStatus.UNAUTHORIZED);
    }

    /**
     * username → membereId로 변경
     */
    @Override
    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter("memberId");
    }
}
