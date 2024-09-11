package site.hesil.latteve_spring.global.rabbitMQ.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * packageName    : site.hesil.latteve_spring.global.rabbitMQ.publisher
 * fileName       : MQSender
 * author         : Yeong-Huns
 * date           : 2024-09-03
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-03        Yeong-Huns       최초 생성
 */
@Log4j2
@RequiredArgsConstructor
@Component
public class MQSender {
    private final RabbitTemplate rabbitTemplate;


    public void sendMessage(String exchange, String routingKey, Object message) {
        log.info("sendMessage : {}", message.toString());
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }

    /*@RabbitListener(queues = "member.create.queue")
    public void receiveMessage(Object Message){
        log.info("receiveMessage : {}", Message.toString());
    }*/
}
