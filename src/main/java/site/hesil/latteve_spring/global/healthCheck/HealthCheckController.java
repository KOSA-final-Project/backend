package site.hesil.latteve_spring.global.healthCheck;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import site.hesil.latteve_spring.global.security.jwt.TokenRepository;
import site.hesil.latteve_spring.global.security.jwt.TokenService;

/**
 * packageName    : site.hesil.latteve_spring
 * fileName       : HealthCheckController
 * author         : yunbin
 * date           : 2024-08-21
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-21           yunbin           최초 생성
 */
@RestController
@RequiredArgsConstructor
public class HealthCheckController {
    private final TokenRepository tokenRepository;
    private final TokenService tokenService;

    @GetMapping("/health-check")
    public String healthCheck() {
        return "up";
    }

    @GetMapping("/test")
    public String healthCheck2(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "null";
        }
        return userDetails.getUsername();
    }

}
