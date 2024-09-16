package site.hesil.latteve_spring.global.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import site.hesil.latteve_spring.domains.member.domain.Member;
import site.hesil.latteve_spring.domains.member.repository.MemberRepository;
import site.hesil.latteve_spring.global.error.exception.BadRequestException;
import site.hesil.latteve_spring.global.error.exception.NotFoundException;
import site.hesil.latteve_spring.global.security.jwt.TokenProvider;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import static site.hesil.latteve_spring.global.security.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

/**
 * packageName    : site.hesil.latteve_spring.global.security
 * fileName       : OAuth2SuccessHandler
 * author         : yunbin
 * date           : 2024-08-23
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-23           yunbin           최초 생성
 * 2024-08-27           yunbin           redirect uri 검증 추가
 * 2024-08-28           yunbin           신규 유저 추가 정보 입력
 * 2024-08-29           yunbin           토큰 발급
 * 2024-09-16           yunbin           쿠키 전달 방식에서 쿼리 파라미터로 변경
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final AppProperties appProperties;
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // accessToken, refreshToken 발급
         String accessToken = tokenProvider.generateAccessToken(authentication);
         tokenProvider.generateRefreshToken(authentication, accessToken);

        // JWT 토큰을 쿠키에 저장
        //tokenProvider.addJwtCookieToResponse(response, accessToken);

        // 사용자 정보 가져오기 (Authentication 객체에서 사용자 정보를 얻음)
        PrincipalDetails oauthUser = (PrincipalDetails)authentication.getPrincipal();

        String email = oauthUser.getUsername();
        String provider = oauthUser.provider();
        log.info(provider);
        String providerId = oauthUser.getName();

        // 사용자가 이미 존재하는지 확인
        Optional<Member> existUser = memberRepository.findByProviderAndProviderId(provider, providerId);
        boolean userExists = existUser.isPresent();
        log.info(""+userExists);

        if (!userExists) {
            // member에 provider, providerId 추가
            Member newUser = memberRepository.findByEmail(email).orElseThrow(NotFoundException::new);

            newUser.updateProvider(provider, providerId);
            memberRepository.save(newUser);
            }

        String targetUrl = determineTargetUrl(request, response, authentication, userExists);
        // access token query parameter로 전달 
        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("accessToken", accessToken)
                .build().toUriString();

        log.info("targetUrl: {}", targetUrl);

        // 응답이 commit 됐으면
        if (response.isCommitted()) {
            log.debug("해당 응답은 이미 처리되었습니다." + targetUrl + "이 주소에 접속할 수 없습니다.");
            return;
        }
        // 인증 과정에서 세션에 저장되었을 수 있는 임시 인증 관련 데이터를 제거합니다.
        clearAuthenticationAttributes(request,response);
        getRedirectStrategy().sendRedirect(request,response,targetUrl);
    }
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication, boolean userExists) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME).map(Cookie::getValue);
        log.info("redirectUri: {}", redirectUri.orElse(null));

        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new BadRequestException("승인되지 않은 redirection uri 입니다.");
        }

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        if (!userExists) {
            targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
                    .queryParam("newUser", true)
                    .build().toUriString();
        }

        return targetUrl;
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return appProperties.getOauth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    // 호스트와 포트만 확인. 만약 클라이언트가 원하는 경우, 다른 경로를 사용하도록 허용한다.
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    if (authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost()) && authorizedURI.getPort() == clientRedirectUri.getPort()) {
                        return true;
                    }
                    return false;
                });
    }

}