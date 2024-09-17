package site.hesil.latteve_spring.global.rabbitMQ.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import site.hesil.latteve_spring.domains.member.domain.Member;
import site.hesil.latteve_spring.domains.project.domain.Project;
import site.hesil.latteve_spring.domains.project.domain.projectLike.ProjectLike;
import site.hesil.latteve_spring.domains.project.domain.projectMember.ProjectMember;
import site.hesil.latteve_spring.domains.project.dto.project.request.AcceptedProjectMemberRequest;
import site.hesil.latteve_spring.domains.search.service.SearchIndexingService;

import java.io.IOException;

/**
 * packageName    : site.hesil.latteve_spring.global.rabbitMQ.consumer
 * fileName       : MQConsumer
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
public class MQConsumer {
    private final SearchIndexingService searchIndexingService;


    @RabbitListener(queues = "#{@projectCreateQueue.name}")
    public void receiveCreateProjectMessage(Project project) throws IOException {
        log.info("rabbit listener : project 인덱싱");
        searchIndexingService.indexProject(project.getProjectId());
    }

    @RabbitListener(queues = "#{@projectUpdateQueue.name}")
    public void receiveUpdateProjectMessage(Project project) throws IOException {
        log.info("rabbit listener : project update 됨");
        searchIndexingService.indexProject(project.getProjectId());
        searchIndexingService.indexMembersToOpenSearch();
    }

    @RabbitListener(queues = "#{@projectLikeCreateQueue.name}")
    public void receiveCreateLikeMessage(ProjectLike projectLike) throws IOException {
        log.info("rabbit listener : project like 인덱싱");
        searchIndexingService.indexProjectLike(projectLike.getProjectLikeId().getProjectId());
    }

    @RabbitListener(queues = "#{@projectMemberUpdateQueue.name}")
    public void receiveUpdateProjectMemberMessage( AcceptedProjectMemberRequest projectMember) throws IOException {
        log.info("rabbit listener : projectMember_update_인덱싱 : member가 승인되어 currentMember에 들어감");
        searchIndexingService.indexProjectMember(projectMember.projectId());
    }

    @RabbitListener(queues = "#{@memberCreateQueue.name}")
    public void receiveCreateMemberMessage(Member member) throws IOException {
        log.info("rabbit listener : member_create 인덱싱");
        searchIndexingService.indexMember(member);

    }
    @RabbitListener(queues = "#{@memberUpdateQueue.name}")
    public void receiveUpdateMessage(Member member) throws IOException {
        searchIndexingService.indexMember(member); // 업데이트 메시지 처리
    }

}
