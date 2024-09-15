package site.hesil.latteve_spring.domains.project.listener;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import site.hesil.latteve_spring.domains.project.domain.Project;
import site.hesil.latteve_spring.domains.project.domain.projectLike.ProjectLike;
import site.hesil.latteve_spring.global.rabbitMQ.enumerate.MQExchange;
import site.hesil.latteve_spring.global.rabbitMQ.enumerate.MQRouting;
import site.hesil.latteve_spring.global.rabbitMQ.publisher.MQSender;

/**
 * packageName    : site.hesil.latteve_spring.domains.project.listener
 * fileName       : ProjectLikeListener
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
public class ProjectLikeListener {
    private final MQSender mqSender;

    @PostPersist
    public void onCreate(ProjectLike projectLike) {
        log.info("ProjectLike created: Project ID = {}, Member ID = {}",  projectLike.getProject().getProjectId(),
                projectLike.getMember().getMemberId());
        log.info("ProjectLike listener: on create ");
        mqSender.sendMessage(MQExchange.DIRECT_PROJECT.getExchange(), MQRouting.PROJECT_LIKE_CREATE.getRouting(), projectLike);
    }

    @PostUpdate
    public void onUpdate(ProjectLike projectLike) {
        mqSender.sendMessage(MQExchange.DIRECT_PROJECT.getExchange(), MQRouting.PROJECT_LIKE_UPDATE.getRouting(), projectLike);
    }

    @PostRemove
    public void onDelete(ProjectLike projectLike) {
        mqSender.sendMessage(MQExchange.DIRECT_PROJECT.getExchange(), MQRouting.PROJECT_LIKE_DELETE.getRouting(), projectLike);
    }
}
