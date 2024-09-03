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
 * fileName       : TopicExchangeConfig
 * author         : Yeong-Huns
 * date           : 2024-09-03
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-03        Yeong-Huns       최초 생성
 */
@Configuration
public class TopicExchangeConfig {
    @Bean // 멤버 u d
    public TopicExchange memberTopicExchange() {
        return new TopicExchange(MQExchange.TOPIC_MEMBER.getExchange());
    }

    /*queue===========================================================*/
    //member
    @Bean
    public Queue memberDeleteQueue(){
        return QueueBuilder.durable(MQueue.MEMBER_DELETE.getQueue())
                .withArgument("x-dead-letter-exchange", MQExchange.DEAD_LETTER.getExchange())
                .withArgument("x-dead-letter-routing-key", MQRouting.DEAD_LETTER.getRouting())
                .build();
    }
    @Bean
    public Queue memberUpdateQueue(){
        return QueueBuilder.durable(MQueue.MEMBER_UPDATE.getQueue())
                .withArgument("x-dead-letter-exchange", MQExchange.DEAD_LETTER.getExchange())
                .withArgument("x-dead-letter-routing-key", MQRouting.DEAD_LETTER.getRouting())
                .build();
    }

    //binding==========================================================
    // member
    @Bean
    public Binding bindingMemberUpdate(@Qualifier("memberTopicExchange") TopicExchange memberTopicExchange,@Qualifier("memberUpdateQueue") Queue memberUpdateQueue) {
        return BindingBuilder.bind(memberUpdateQueue).to(memberTopicExchange).with(MQRouting.MEMBER_UPDATE.getRouting());
    }
    @Bean
    public Binding bindingMemberDelete(@Qualifier("memberTopicExchange") TopicExchange memberTopicExchange,@Qualifier("memberDeleteQueue") Queue memberDeleteQueue) {
        return BindingBuilder.bind(memberDeleteQueue).to(memberTopicExchange).with(MQRouting.MEMBER_DELETE.getRouting());
    }

}
