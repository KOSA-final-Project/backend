package site.hesil.latteve_spring.global.security.jwt;

/**
 *packageName    : site.hesil.latteve_spring.global.security
 * fileName       : TokenAuthenticationFilter
 * author         : yunbin
 * date           : 2024-08-29
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-29           yunbin           최초 생성
 * 2024-08-30           yunbin           access token 만료시 재발급된 token이 쿠키에 저장 안되는 문제 해결
 * 2024-09-16           yunbin           쿠키말고 응답 헤더에서 토큰 얻음
 */
//@Slf4j
//@RequiredArgsConstructor
//@Component
//public class TokenAuthenticationFilter extends OncePerRequestFilter {
//
//    private final TokenProvider tokenProvider;
//    private static final String TOKEN_PREFIX = "Bearer ";
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//        log.info("토큰 검증 필터");
//        String accessToken = resolveToken(request);
//
//        // accessToken 검증
//        if (tokenProvider.validateToken(accessToken)) {
//            setAuthentication(accessToken);
//            log.info("토큰 검증 완료");
//        } else {
//            // 만료되었을 경우 accessToken 재발급
//            String reissueAccessToken = tokenProvider.reissueAccessToken(accessToken);
//            log.info("재발급 토큰 " + reissueAccessToken);
//
//            if (StringUtils.hasText(reissueAccessToken)) {
//                setAuthentication(reissueAccessToken);
//
//                // 재발급된 accessToken 다시 전달
//                // tokenProvider.addJwtCookieToResponse(response, reissueAccessToken);
//
//                // 재발급된 accessToken을 응답 헤더에 추가
//                response.setHeader("Authorization", TOKEN_PREFIX + reissueAccessToken);
//
//                log.info("토큰 재발급 완료");
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
//    private void setAuthentication(String accessToken) {
//        Authentication authentication = tokenProvider.getAuthentication(accessToken);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//    }
//
//    private String resolveToken(HttpServletRequest request) {
////        Cookie[] cookies = request.getCookies();
////
////        if (cookies != null) {
////            for (Cookie cookie : cookies) {
////                if ("jwt".equals(cookie.getName())) { // 쿠키 이름을 "jwt"로 가정
////                    String cookieToken = cookie.getValue();
////                    if (StringUtils.hasText(cookieToken)) {
////                        log.info("resolveToken {}",  cookieToken);
////                        return cookieToken;
////                    }
////                }
////            }
////        }
////        log.info("토큰전달안됨 {}", request.getRequestURI());
////        return null;
//
//        // Authorization 헤더에서 JWT 토큰 추출
//        String bearerToken = request.getHeader("Authorization");
//        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
//            return bearerToken.substring(TOKEN_PREFIX.length()); // "Bearer " 이후의 토큰 값 반환
//        }
//        log.info("Authorization 헤더에 토큰 없음");
//        return null;
//    }
//}