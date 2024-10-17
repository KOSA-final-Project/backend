package site.hesil.latteve_spring.global.security.ArgumentResolver;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import site.hesil.latteve_spring.global.error.errorcode.ErrorCode;
import site.hesil.latteve_spring.global.error.exception.CustomBaseException;
import site.hesil.latteve_spring.global.security.annotation.AuthMemberId;
import site.hesil.latteve_spring.global.security.jwt.TokenProvider;

import java.util.Optional;

/**
 * packageName    : site.hesil.latteve_spring.global.security.ArgumentResolver
 * fileName       : AuthMemberResolver
 * author         : Yeong-Huns
 * date           : 2024-09-04
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-04        Yeong-Huns       최초 생성
 * 2024-09-16        yunbin           쿠키말고 응답 헤더에서 토큰 얻음
 */
@Log4j2
@RequiredArgsConstructor
@Component
public class AuthMemberResolver implements HandlerMethodArgumentResolver {

    private final TokenProvider tokenProvider;
    private static final String TOKEN_PREFIX = "Bearer ";

    @Override
    public boolean supportsParameter(@NonNull MethodParameter parameter) {
        return parameter.getParameterType().equals(Long.class) &&
                parameter.hasParameterAnnotation(AuthMemberId.class);
    }

    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = extractTokenFromCookies(request);
        Optional<Long> memberId = Optional.empty();
        if (token != null) {
            memberId = tokenProvider.getMemberId(token);
        }
        return memberId.orElseThrow(()->new CustomBaseException(ErrorCode.TOKEN_NOT_FOUND));
    }

    private String extractTokenFromCookies(HttpServletRequest request) {
//        Cookie[] cookies = request.getCookies();
//
//        log.info(Arrays.toString(cookies));
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if ("jwt".equals(cookie.getName())) {
//                    return cookie.getValue();
//                }
//            }
//        }
//        log.error("쿠키에서 토큰 추출 중 에러");
//        return null;

        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length()); // "Bearer " 이후의 토큰 값 반환
        }
        log.info("Authorization 헤더에 토큰 없음");
        return null;
    }
}