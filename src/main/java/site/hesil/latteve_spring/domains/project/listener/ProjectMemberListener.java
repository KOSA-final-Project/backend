package site.hesil.latteve_spring.domains.project.listener;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import site.hesil.latteve_spring.domains.project.domain.Project;
import site.hesil.latteve_spring.domains.project.domain.projectMember.ProjectMember;
import site.hesil.latteve_spring.global.rabbitMQ.enumerate.MQExchange;
import site.hesil.latteve_spring.global.rabbitMQ.enumerate.MQRouting;
import site.hesil.latteve_spring.global.rabbitMQ.publisher.MQSender;

/**
 * packageName    : site.hesil.latteve_spring.domains.project.listener
 * fileName       : ProjectMemberListener
 * author         : Heeseon
 * date           : 2024-09-14
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-14        Heeseon       최초 생성
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectMemberListener {
    private final MQSender mqSender;

    @PostPersist
    public void onCreate(ProjectMember projectMember) {
        mqSender.sendMessage(MQExchange.DIRECT_PROJECT.getExchange(), MQRouting.PROJECT_MEMBER_CREATE.getRouting(),projectMember);
    }

    @PostUpdate
    public void onUpdate(ProjectMember projectMember) {
        log.info("projectmemberListener : projectMember update");
        mqSender.sendMessage(MQExchange.DIRECT_PROJECT.getExchange(), MQRouting.PROJECT_MEMBER_UPDATE.getRouting(), projectMember);
    }

    @PostRemove
    public void onDelete(ProjectMember projectMember) {
        mqSender.sendMessage(MQExchange.DIRECT_PROJECT.getExchange(), MQRouting.PROJECT_MEMBER_DELETE.getRouting(), projectMember);
    }
}
