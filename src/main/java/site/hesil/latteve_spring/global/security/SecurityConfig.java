package site.hesil.latteve_spring.global.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import site.hesil.latteve_spring.global.security.jwt.TokenAuthenticationFilter;
import site.hesil.latteve_spring.global.security.jwt.TokenExceptionFilter;

/**
 * packageName    : site.hesil.latteve_spring.global.security
 * fileName       : SecurityConfig
 * author         : yunbin
 * date           : 2024-08-23
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-23           yunbin           최초 생성
 * 2024-08-29           yunbin           jwt 관련 설정 추가
 * 2024-09-09           Heeseon          swagger-ui 경로 추가
 */

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Log4j2
public class SecurityConfig {

    private final CustomOAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailHandler oAuth2FailHandler;
    private final TokenAuthenticationFilter tokenAuthenticationFilter;

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieOAuth2AuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() { // security를 적용하지 않을 리소스
        return web -> web.ignoring()
                // error endpoint를 열어줘야 함, favicon.ico 추가!
                .requestMatchers("/error", "/favicon.ico");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // rest api 설정
//                .csrf(csrf -> csrf
//                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // 쿠키에 CSRF 토큰을 저장
//                )
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable) // 기본 인증 로그인 비활성화
                .formLogin(AbstractHttpConfigurer::disable) // 기본 login form 비활성화
                .logout(AbstractHttpConfigurer::disable) // 기본 logout 비활성화
                .headers(c -> c.frameOptions(
                        FrameOptionsConfig::disable).disable()) // X-Frame-Options 비활성화
                .sessionManagement(c ->
                        c.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용하지 않음

                // request 인증, 인가 설정
                .authorizeHttpRequests(request ->
                        request
                                .requestMatchers(
//                                        new AntPathRequestMatcher("/"),
//                                        new AntPathRequestMatcher("/error"),
//                                        new AntPathRequestMatcher("/favicon.ico"),
//                                        new AntPathRequestMatcher("/oauth2/**"),
//                                        new AntPathRequestMatcher("/login/**"),
//                                        new AntPathRequestMatcher("/swagger-ui.html/**", " /v3/api-docs/**")
                                        new AntPathRequestMatcher("HttpMethod.OPTIONS", "/**")
                                ).permitAll()
                                .requestMatchers(
                                        new AntPathRequestMatcher("/users/check-nickname"),
                                        new AntPathRequestMatcher("/users/me"),
                                        new AntPathRequestMatcher("/projects"),
                                        new AntPathRequestMatcher("/projects/start"),
                                        new AntPathRequestMatcher("/projects/my/like"),
                                        new AntPathRequestMatcher("/projects/*/like"),
                                        new AntPathRequestMatcher("/projects/my"),
                                        new AntPathRequestMatcher("/projects/*/participation"),
                                        new AntPathRequestMatcher("/projects/*/applications", "HttpMethod.POST"),
                                        new AntPathRequestMatcher("/projects/applications"),
                                        new AntPathRequestMatcher("/projects/*/applications/*"),
                                        new AntPathRequestMatcher("/projects/*/retrospectives", "HttpMethod.POST"),
                                        new AntPathRequestMatcher("/projects/*/retrospectives/*"),
                                        new AntPathRequestMatcher("/projects/notifications")
                                ).authenticated()
                                .requestMatchers(
                                        new AntPathRequestMatcher("/**")                // permitAll fallback
                                ).permitAll()
                )

                // oauth2 설정
                .oauth2Login(oauth -> // OAuth2 로그인 기능에 대한 여러 설정의 진입점
                        // OAuth2 로그인 성공 이후 사용자 정보를 가져올 때의 설정을 담당
                        oauth.userInfoEndpoint(c -> c.userService(oAuth2UserService))
                                .authorizationEndpoint(a -> {
                                            a.baseUri("/oauth2/authorize")
                                                    .authorizationRequestRepository(cookieOAuth2AuthorizationRequestRepository());
                                        }
                                )
                                .redirectionEndpoint(a -> {
                                            a.baseUri("/login/oauth2/code/**");
                                        }
                                )
                                // 로그인 성공 시 핸들러
                                .successHandler(oAuth2SuccessHandler)
                                .failureHandler(oAuth2FailHandler)

                )

                // jwt 관련 설정
                .addFilterBefore(tokenAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new TokenExceptionFilter(), tokenAuthenticationFilter.getClass()) // 토큰 예외 핸들링

                // 인증 예외 핸들링
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                        .accessDeniedHandler(new CustomAccessDeniedHandler()));

        return http.build();
    }
}