package site.hesil.latteve_spring.domains.project.listener;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.hesil.latteve_spring.domains.project.domain.Project;
import site.hesil.latteve_spring.global.rabbitMQ.enumerate.MQExchange;
import site.hesil.latteve_spring.global.rabbitMQ.enumerate.MQRouting;
import site.hesil.latteve_spring.global.rabbitMQ.publisher.MQSender;

/**
 * packageName    : site.hesil.latteve_spring.domains.project.service
 * fileName       : ProjectEventListener
 * author         : Heeseon
 * date           : 2024-09-14
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-14        Heeseon       최초 생성
 */
@Component
@RequiredArgsConstructor
public class ProjectListener {

    private final MQSender mqSender;

    @PostPersist
    public void onCreate(Project project) {
        mqSender.sendMessage(MQExchange.DIRECT_PROJECT.getExchange(), MQRouting.PROJECT_CREATE.getRouting(), project);
    }

    @PostUpdate
    public void onUpdate(Project project) {
        mqSender.sendMessage(MQExchange.DIRECT_PROJECT.getExchange(), MQRouting.PROJECT_UPDATE.getRouting(), project);
    }

    @PostRemove
    public void onDelete(Project project) {
        mqSender.sendMessage(MQExchange.DIRECT_PROJECT.getExchange(), MQRouting.PROJECT_DELETE.getRouting(), project);
    }
}