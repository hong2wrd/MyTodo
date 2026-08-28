package side.todo.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import side.todo.security.MemberDetails;

import java.io.IOException;
import java.util.List;

/**
 * 모든 요청 URL 검증(토큰)
 */
@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final JwtProperties jwtProperties;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtProperties jwtProperties) {
        super(authenticationManager);
        this.jwtProperties = jwtProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 토근 확인 예외
        if(isExclusionRequest(request)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            // 토큰 확인
            if(isHeaderVerify(request)) {
                String token = request.getHeader(jwtProperties.getAccessTokenHeader());
                MemberDetails userDetails = (MemberDetails) JwtUtil.verity(token, jwtProperties);

                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (AuthenticationException e) {
            log.error(e.getMessage(), e);
        }

        chain.doFilter(request, response);
    }

    private boolean isHeaderVerify(HttpServletRequest request) {
        String header = request.getHeader(jwtProperties.getAccessTokenHeader());
        return header != null && header.startsWith(jwtProperties.getPrefix());
    }

    /**
     * 예외 URL 요청 확인
     * @param request
     * @return
     */
    private boolean isExclusionRequest(HttpServletRequest request){
        return List.of("/auth/refresh", "/logout").contains(request.getRequestURI());
    }
}
