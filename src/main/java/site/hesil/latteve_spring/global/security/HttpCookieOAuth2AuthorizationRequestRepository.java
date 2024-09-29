package site.hesil.latteve_spring.global.security;

/**
 * packageName    : site.hesil.latteve_spring.global.security
 * fileName       : HttpCookieOAuth2AuthorizationRequestRepository
 * author         : yunbin
 * date           : 2024-08-27
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-27           yunbin           최초 생성
 */
//@Component
//@Log4j2
//public class HttpCookieOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
//    public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
//    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
//    private static final int cookieExpireSeconds = 180;
//
//    // OAuth 2.0 인증 쿠키 불러오기
//    @Override
//    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
//        log.info("loadAuthorizationRequest 실행됨");
//        return CookieUtils.getCookie(request,OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
//                .map(cookie -> CookieUtils.deserialize(cookie,OAuth2AuthorizationRequest.class))
//                .orElse(null);
//    }
//
//    // Authorization Request 요청 저장하기
//    @Override
//    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
//        log.info("saveAuthorizationRequest 실행됨");
//        if (authorizationRequest == null) {
//            CookieUtils.deleteCookie(request,response,OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
//            CookieUtils.deleteCookie(request,response,REDIRECT_URI_PARAM_COOKIE_NAME);
//            return;
//        }
//        CookieUtils.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME,CookieUtils.serialize(authorizationRequest), cookieExpireSeconds);
//        // 파라미터 값 가져오기
//        String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
//        log.info("redirectUriAfterLogin: " + redirectUriAfterLogin);
//
//        if (StringUtils.isNotBlank(redirectUriAfterLogin)) {
//            CookieUtils.addCookie(response,REDIRECT_URI_PARAM_COOKIE_NAME,redirectUriAfterLogin,cookieExpireSeconds);
//        }
//    }
//
//    // OAuth 2.0 인증 요청 지우기
//    @Override
//    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
//        log.info("removeAuthorizationRequest 실행됨");
//        return this.loadAuthorizationRequest(request);
//    }
//
//    // 쿠키 지우기
//    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
//        log.info("removeAuthorizationRequestCookies 실행됨");
//        CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
//        CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
//    }
//}
