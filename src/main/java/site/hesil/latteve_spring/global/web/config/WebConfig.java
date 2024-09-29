package site.hesil.latteve_spring.global.web.config;

/**
 * packageName    : site.hesil.latteve_spring.global.web.config
 * fileName       : WebConfig
 * author         : yunbin
 * date           : 2024-08-27
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-27           yunbin           최초 생성
 * 2024-09-03           yeong-huns       addArgumentResolvers 메서드 추가
 */
//@RequiredArgsConstructor
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//    private final AuthMemberResolver authMemberResolver;
//    private final long MAX_AGE_SECS = 3600;
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        // cors 적용할 url 패턴
//        registry.addMapping("/**")
//                // 자원을 공유할 오리진 지정
//                .allowedOrigins("http://localhost:3000", "https://latteve.site")
//                // 요청 허용 메서드
//                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
//                // 요청 허용 헤더
//                .allowedHeaders("*")
//                // 쿠키 사용
//                .allowCredentials(true)
//                .maxAge(MAX_AGE_SECS);
//    }
//
//    @Override
//    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
//        resolvers.add(authMemberResolver);
//    }
//
//}
