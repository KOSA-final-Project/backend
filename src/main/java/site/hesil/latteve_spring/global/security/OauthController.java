package site.hesil.latteve_spring.global.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * packageName    : site.hesil.latteve_spring.global.security
 * fileName       : OauthController
 * author         : yunbin
 * date           : 2024-08-27
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-27           yunbin           최초 생성
 */
@RestController
@Slf4j
public class OauthController {

    @GetMapping("/oauth2/authorization/google")
    public String handleCallback(@RequestParam Map<String, String> params) {
        log.debug("여기까진되나봄?");
        // 처리 로직
        return "여기까진되나봄?"; // 또는 다른 뷰 이름
    }
}
