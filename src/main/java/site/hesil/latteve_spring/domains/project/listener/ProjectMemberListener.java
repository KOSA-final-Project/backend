package site.hesil.latteve_spring.domains.project.listener;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;
import site.hesil.latteve_spring.domains.project.domain.Project;
import site.hesil.latteve_spring.domains.project.domain.projectMember.ProjectMember;
import site.hesil.latteve_spring.domains.project.dto.project.request.AcceptedProjectMemberRequest;
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
        try {
            log.info("ProjectMember create: Project ID = {}, Member ID = {}",  projectMember.getProject().getProjectId(),projectMember.getMember().getMemberId());
            mqSender.sendMessage(MQExchange.DIRECT_PROJECT.getExchange(), MQRouting.PROJECT_MEMBER_CREATE.getRouting(), projectMember);
        } catch (Exception e) {
            log.error("Error sending create message to MQ for projectMember: {}", projectMember.getProjectMemberId(), e);
        }
    }

    @PostUpdate
    public void onUpdate(ProjectMember projectMember) {
        try {
            log.info("ProjectMember update: Project ID = {}, Member ID = {}, Job ID = {}",  projectMember.getProject().getProjectId(),projectMember.getMember().getMemberId(), projectMember.getJob().getJobId());
            AcceptedProjectMemberRequest projectMemberRequest = AcceptedProjectMemberRequest.builder()
                    .projectId(projectMember.getProject().getProjectId())
                    .memberId(projectMember.getMember().getMemberId())
                    .jobId(projectMember.getJob().getJobId())
                    .build();
            mqSender.sendMessage(MQExchange.DIRECT_PROJECT.getExchange(), MQRouting.PROJECT_MEMBER_UPDATE.getRouting(),  projectMemberRequest);
            log.info("update 후");
        } catch (Exception e) {
            log.error("Error sending update message to MQ for Project ID = {}, Member ID = {}",
                    projectMember.getProject().getProjectId(), projectMember.getMember().getMemberId(), e);

        }
    }

    @PostRemove
    public void onDelete(ProjectMember projectMember) {
        try {
            log.info("ProjectMember delete: Project ID = {}, Member ID = {}",  projectMember.getProject().getProjectId(),projectMember.getMember().getMemberId());
            mqSender.sendMessage(MQExchange.DIRECT_PROJECT.getExchange(), MQRouting.PROJECT_MEMBER_DELETE.getRouting(), projectMember);
        } catch (Exception e) {
            log.error("Error sending delete message to MQ for projectMember: {}", projectMember.getProjectMemberId(), e);
        }
    }
}
