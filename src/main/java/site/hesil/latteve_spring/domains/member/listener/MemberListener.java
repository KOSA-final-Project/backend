package site.hesil.latteve_spring.domains.member.listener;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.hesil.latteve_spring.domains.member.domain.Member;
import site.hesil.latteve_spring.domains.project.domain.Project;
import site.hesil.latteve_spring.global.rabbitMQ.enumerate.MQExchange;
import site.hesil.latteve_spring.global.rabbitMQ.enumerate.MQRouting;
import site.hesil.latteve_spring.global.rabbitMQ.publisher.MQSender;

/**
 * packageName    : site.hesil.latteve_spring.domains.member.listener
 * fileName       : MemberListener
 * author         : Heeseon
 * date           : 2024-09-16
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-16        Heeseon       최초 생성
 */
@Component
@RequiredArgsConstructor
public class MemberListener {

    private final MQSender mqSender;

    @PostPersist
    public void onCreate(Member member)  {
        mqSender.sendMessage(MQExchange.DIRECT_MEMBER.getExchange(), MQRouting.MEMBER_CREATE.getRouting(), member);
    }

    @PostUpdate
    public void onUpdate(Member member) {
        mqSender.sendMessage(MQExchange.DIRECT_MEMBER.getExchange(), MQRouting.MEMBER_UPDATE.getRouting(), member);
    }

    @PostRemove
    public void onDelete(Member member) {
        mqSender.sendMessage(MQExchange.DIRECT_MEMBER.getExchange(), MQRouting.MEMBER_DELETE.getRouting(), member);
    }
}
