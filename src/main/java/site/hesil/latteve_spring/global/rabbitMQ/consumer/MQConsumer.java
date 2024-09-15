package site.hesil.latteve_spring.global.rabbitMQ.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import site.hesil.latteve_spring.domains.project.domain.Project;
import site.hesil.latteve_spring.domains.project.domain.projectLike.ProjectLike;
import site.hesil.latteve_spring.domains.project.domain.projectMember.ProjectMember;
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

    @RabbitListener(queues = "#{@projectLikeCreateQueue.name}")
    public void receiveCreateLikeMessage(ProjectLike projectLike) throws IOException {
        log.info("rabbit listener : project like 인덱싱");
        searchIndexingService.indexProjectLike(projectLike.getProject().getProjectId());
    }

    @RabbitListener(queues = "#{@projectMemberCreateQueue.name}")
    public void receiveCreateMemberMessage(ProjectMember projectMember) throws IOException {
        log.info("rabbit listener : project member 인덱싱");
        searchIndexingService.indexProjectMember(projectMember.getProject().getProjectId());
    }

//    @RabbitListener(queues = "project.update.queue")
//    public void receiveUpdateMessage(Project project) {
//        searchIndexingService.updateProject(project);  // 업데이트 메시지 처리
//    }
//
//    @RabbitListener(queues = "project.delete.queue")
//    public void receiveDeleteMessage(Project project) {
//        searchIndexingService.deleteProject(project.getId());  // 삭제 메시지 처리
//    }
}
