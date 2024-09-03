package site.hesil.latteve_spring.global.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * packageName    : site.hesil.latteve_spring.global.security
 * fileName       : CustomAuthenticationEntryPoint
 * author         : yunbin
 * date           : 2024-08-23
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-23           yunbin           최초 생성
 */
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        log.error("AuthenticationException is occurred. ", authException);
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "인증에 실패하였습니다.");
    }
}