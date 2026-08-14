package side.todo.util;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import side.todo.dto.ResponseDto;
import tools.jackson.databind.ObjectMapper;

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
}
