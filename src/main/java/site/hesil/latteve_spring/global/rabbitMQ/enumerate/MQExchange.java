package site.hesil.latteve_spring.global.rabbitMQ.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * packageName    : site.hesil.latteve_spring.global.rabbitMQ.enumerate
 * fileName       : RabbitMQ
 * author         : Yeong-Huns
 * date           : 2024-09-03
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-03        Yeong-Huns       최초 생성
 */
@Getter
@RequiredArgsConstructor
public enum MQExchange {
    DIRECT_MEMBER("memberDirectExchange"),
    DEAD_LETTER("deadLetterExchange"),
    TOPIC_MEMBER("topicMemberExchange"),
    ;


    private final String exchange;
}
