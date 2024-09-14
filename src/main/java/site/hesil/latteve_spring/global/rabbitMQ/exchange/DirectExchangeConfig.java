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
        return BindingBuilder.bind(memberCreateQueue).to(memberDirectExchange).with(MQRouting.MEMBER_CREATE.getRouting());
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




// project 관련 메시지 처리

    @Bean
    public DirectExchange projectExchange() {
        return new DirectExchange(MQExchange.DIRECT_PROJECT.getExchange());
    }

    @Bean
    public Queue projectCreateQueue(){
        return QueueBuilder.durable(MQueue.PROJECT_CREATE.getQueue())
                .withArgument("x-dead-letter-exchange", MQExchange.DEAD_LETTER.getExchange())
                .withArgument("x-dead-letter-routing-key", MQRouting.DEAD_LETTER.getRouting())
                .build();

    }
    @Bean
    public Queue projectLikeCreateQueue(){
        return QueueBuilder.durable(MQueue.PROJECT_LIKE_CREATE.getQueue())
                .withArgument("x-dead-letter-exchange", MQExchange.DEAD_LETTER.getExchange())
                .withArgument("x-dead-letter-routing-key", MQRouting.DEAD_LETTER.getRouting())
                .build();

    }
    @Bean
    public Queue projectMemberCreateQueue(){
        return QueueBuilder.durable(MQueue.PROJECT_MEMBER_CREATE.getQueue())
                .withArgument("x-dead-letter-exchange", MQExchange.DEAD_LETTER.getExchange())
                .withArgument("x-dead-letter-routing-key", MQRouting.DEAD_LETTER.getRouting())
                .build();

    }
    // 프로젝트 업데이트 큐
    @Bean
    public Queue projectUpdateQueue() {
        return QueueBuilder.durable(MQueue.PROJECT_UPDATE.getQueue())
                .withArgument("x-dead-letter-exchange", MQExchange.DEAD_LETTER.getExchange())
                .withArgument("x-dead-letter-routing-key", MQRouting.DEAD_LETTER.getRouting())
                .build();
    }

    // 프로젝트 좋아요 업데이트 큐
    @Bean
    public Queue projectLikeUpdateQueue() {
        return  QueueBuilder.durable(MQueue.PROJECT_LIKE_UPDATE.getQueue())
                .withArgument("x-dead-letter-exchange", MQExchange.DEAD_LETTER.getExchange())
                .withArgument("x-dead-letter-routing-key", MQRouting.DEAD_LETTER.getRouting())
                .build();
    }

    // 프로젝트 멤버 업데이트 큐
    @Bean
    public Queue projectMemberUpdateQueue() {
        return QueueBuilder.durable(MQueue.PROJECT_MEMBER_UPDATE.getQueue())
                .withArgument("x-dead-letter-exchange", MQExchange.DEAD_LETTER.getExchange())
                .withArgument("x-dead-letter-routing-key", MQRouting.DEAD_LETTER.getRouting())
                .build();
    }

    // 바인딩: 큐를 교환기에 바인딩
    @Bean
    public Binding bindingProjectCreate(DirectExchange projectExchange, Queue projectCreateQueue) {
        return BindingBuilder.bind(projectCreateQueue).to(projectExchange).with(MQRouting.PROJECT_CREATE.getRouting());
    }

    @Bean
        public Binding bindingProjectLikeCreate(DirectExchange projectExchange, Queue projectLikeCreateQueue) {
        return BindingBuilder.bind(projectLikeCreateQueue).to(projectExchange).with(MQRouting.PROJECT_LIKE_CREATE.getRouting());
    }

    @Bean
    public Binding bindingProjectMemberCreate(DirectExchange projectExchange, Queue projectMemberCreateQueue) {
        return BindingBuilder.bind(projectMemberCreateQueue).to(projectExchange).with(MQRouting.PROJECT_MEMBER_CREATE.getRouting());
    }

    @Bean
    public Binding bindingProjectUpdate(DirectExchange projectExchange, Queue projectUpdateQueue) {
        return BindingBuilder.bind(projectUpdateQueue).to(projectExchange).with(MQRouting.PROJECT_UPDATE.getRouting());
    }

    @Bean
    public Binding bindingProjectLikeUpdate(DirectExchange projectExchange, Queue projectLikeUpdateQueue) {
        return BindingBuilder.bind(projectLikeUpdateQueue).to(projectExchange).with(MQRouting.PROJECT_LIKE_UPDATE.getRouting());
    }

    @Bean
    public Binding bindingProjectMemberUpdate(DirectExchange projectExchange, Queue projectMemberUpdateQueue) {
        return BindingBuilder.bind(projectMemberUpdateQueue).to(projectExchange).with(MQRouting.PROJECT_MEMBER_UPDATE.getRouting());
    }

}
