package site.hesil.latteve_spring.global.rabbitMQ.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * packageName    : site.hesil.latteve_spring.global.rabbitMQ.enumerate
 * fileName       : Queue
 * author         : Yeong-Huns
 * date           : 2024-09-03
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-03        Yeong-Huns       최초 생성
 */
@Getter
@RequiredArgsConstructor
public enum MQueue {
    MEMBER_CREATE("member.create.queue"),
    DEAD_LETTER("deadLetter.queue"),
    MEMBER_DELETE("member.delete.queue"),
    MEMBER_UPDATE("member.update.queue")
    ;

    private final String queue;
}
