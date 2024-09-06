package site.hesil.latteve_spring.global.rabbitMQ.health;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import site.hesil.latteve_spring.global.rabbitMQ.enumerate.MQExchange;
import site.hesil.latteve_spring.global.rabbitMQ.enumerate.MQRouting;
import site.hesil.latteve_spring.global.rabbitMQ.publisher.MQSender;

/**
 * packageName    : site.hesil.latteve_spring.global.rabbitMQ.health
 * fileName       : HealthCheck
 * author         : Yeong-Huns
 * date           : 2024-09-05
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-05        Yeong-Huns       최초 생성
 */
@Log4j2
@RequiredArgsConstructor
@Controller
public class HealthCheck {
    private final MQSender sender;

    @GetMapping("/send/message")
    public void testConnection(){
        log.info("테스팅");
        sender.sendMessage(MQExchange.DIRECT_MEMBER.getExchange(), MQRouting.MEMBER_CREATE.getRouting(), "test");
        log.info("래빗MQ 전송성공");
    }


}
