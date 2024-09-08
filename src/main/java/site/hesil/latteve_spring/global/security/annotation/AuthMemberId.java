package site.hesil.latteve_spring.global.security.annotation;

/**
 * packageName    : site.hesil.latteve_spring.global.security.annotation
 * fileName       : AuthMemberId
 * author         : Yeong-Huns
 * date           : 2024-09-04
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-04        Yeong-Huns       최초 생성
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthMemberId {
    // 해당 어노테이션을 사용하면 자동으로 토큰에서 MemberId 를 추출해서 주입
}
