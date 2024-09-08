package site.hesil.latteve_spring.global.security.ArgumentResolver;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import site.hesil.latteve_spring.global.error.errorcode.ErrorCode;
import site.hesil.latteve_spring.global.error.exception.CustomBaseException;
import site.hesil.latteve_spring.global.error.exception.TokenException;
import site.hesil.latteve_spring.global.security.annotation.AuthMemberId;
import site.hesil.latteve_spring.global.security.annotation.LoginFilterMemberId;
import site.hesil.latteve_spring.global.security.jwt.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

/**
 * packageName    : site.hesil.latteve_spring.global.security.ArgumentResolver
 * fileName       : LoginFilterMemberIdResolver
 * author         : Heeseon
 * date           : 2024-09-08
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-08        Heeseon       최초 생성
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class LoginFilterMemberIdResolver implements HandlerMethodArgumentResolver {
    private final TokenProvider tokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Long.class) &&
                parameter.hasParameterAnnotation(LoginFilterMemberId.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
            throws Exception {
        LoginFilterMemberId loginFilterMemberId = parameter.getParameterAnnotation(LoginFilterMemberId.class);

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = extractJwtFromRequest(request);

        if (token == null) {
            if (loginFilterMemberId.required()) {
                log.error("resolveArgument 토큰 파싱중 null 반환");
                throw new CustomBaseException(ErrorCode.TOKEN_NOT_FOUND);  // 예외를 반환하지 말고 던짐
            } else {
                // required=false일 경우 null 반환 (비로그인 사용자의 경우)
                return null;
            }
        }

        // JWT에서 loginFilterMemberId 추출
        Optional<Long> memberId = tokenProvider.getMemberId(token);
        return memberId.orElseThrow(() -> new CustomBaseException(ErrorCode.TOKEN_INVALID));
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        log.info(Arrays.toString(cookies));
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        log.error("쿠키에서 토큰 추출 중 에러");
        return null;
    }
}
