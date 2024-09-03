package site.hesil.latteve_spring.global.rabbitMQ.exchange;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import site.hesil.latteve_spring.global.rabbitMQ.enumerate.MQExchange;
import site.hesil.latteve_spring.global.rabbitMQ.enumerate.MQRouting;
import site.hesil.latteve_spring.global.rabbitMQ.enumerate.MQueue;

/**
 * packageName    : site.hesil.latteve_spring.global.rabbitMQ.exchange
 * fileName       : DirectExchangeConfig
 * author         : Yeong-Huns
 * date           : 2024-09-03
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-03        Yeong-Huns       최초 생성
 */
@Configuration
public class DirectExchangeConfig {
    @Bean
    public DirectExchange memberDirectExchange() {
        return new DirectExchange(MQExchange.DIRECT_MEMBER.getExchange());
    }
    @Bean
    public Queue memberCreateQueue(){
        return QueueBuilder.durable(MQueue.MEMBER_CREATE.getQueue())
                .withArgument("x-dead-letter-exchange", MQExchange.DEAD_LETTER.getExchange())
                .withArgument("x-dead-letter-routing-key", MQRouting.DEAD_LETTER.getRouting())
                .build();

    }
    @Bean
    public Binding bindingMemberCreate(@Qualifier("memberDirectExchange") DirectExchange memberDirectExchange, @Qualifier("memberCreateQueue") Queue memberCreateQueue){
        return BindingBuilder.bind(memberCreateQueue).to(memberDirectExchange).with(MQRouting.MEMBER_CREATE .getRouting());
    }

    //DeadLetter -> 메세지 실패처리
    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(MQExchange.DEAD_LETTER.getExchange());
    }
    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(MQueue.DEAD_LETTER.getQueue())
                .withArgument("x-dead-letter-exchange", MQExchange.DEAD_LETTER.getExchange())
                .withArgument("x-dead-letter-routing-key", MQRouting.DEAD_LETTER.getRouting())
                .build();
    }
    @Bean
    public Binding bindingDeadLetter(@Qualifier("deadLetterExchange") DirectExchange deadLetterExchange, @Qualifier("deadLetterQueue") Queue deadLetterQueue){
        return BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange).with(MQRouting.DEAD_LETTER.getRouting());
    }

}
