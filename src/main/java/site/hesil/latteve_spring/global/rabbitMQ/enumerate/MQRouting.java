package site.hesil.latteve_spring.global.rabbitMQ.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * packageName    : site.hesil.latteve_spring.global.rabbitMQ.enumerate
 * fileName       : MQRouting
 * author         : Yeong-Huns
 * date           : 2024-09-03
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-03        Yeong-Huns       최초 생성
 */
@Getter
@RequiredArgsConstructor
public enum MQRouting {
    MEMBER_CREATE("member.create"),
    DEAD_LETTER("deadLetter"),
    MEMBER_UPDATE("member.update"),
    MEMBER_DELETE("member.delete"),
    APPLICATION_CREATE("application"),
    APPROVAL_RESULT("approval"),
    ;
    private final String routing;
}
