package side.todo.util;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import side.todo.dto.ResponseDto;
import side.todo.security.jwt.JwtProperties;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;

@Slf4j
public class ResponseUtil {

    public static void response(HttpServletResponse response, ResponseDto<?> responseDto, HttpStatus httpStatus) {
        try {
            ObjectMapper om = new ObjectMapper();
            String responseBody = om.writeValueAsString(responseDto);
            response.setContentType("application/json; charset=UTF-8");
            response.setStatus(httpStatus.value());
            response.getWriter().println(responseBody);
        } catch (Exception e) {
            log.error("Server Parsing Error");
        }
    }

    public static void removeCookie(HttpServletResponse response, JwtProperties jwtProperties, boolean secure) {
        ResponseCookie cookie = ResponseCookie
                .from(jwtProperties.getRefreshTokenHeader(), "")
                .httpOnly(secure)
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ZERO)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
