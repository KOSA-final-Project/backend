package site.hesil.latteve_spring.global.healthCheck;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class HealthCheckController {
    @GetMapping("/health-check")
    public String healthCheck() {
        return "up";
    }
}
