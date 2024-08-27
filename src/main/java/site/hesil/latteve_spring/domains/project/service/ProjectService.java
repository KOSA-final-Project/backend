package site.hesil.latteve_spring.domains.project.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import site.hesil.latteve_spring.domains.project.dto.project.response.ProjectDetailResponse;
import site.hesil.latteve_spring.domains.project.repository.ProjectRepository;

/**
 * packageName    : site.hesil.latteve_spring.domains.project.service
 * fileName       : ProjectService
 * author         : JooYoon
 * date           : 2024-08-26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-26        JooYoon       최초 생성
 */

@Service
@RequiredArgsConstructor
public class ProjectService {

    private static final Logger log = LoggerFactory.getLogger(ProjectService.class);
    private final ProjectRepository projectRepository;

    public ProjectDetailResponse getProjectDetail(Long projectId) {
        ProjectDetailResponse projectDetailResponse = projectRepository.getProjectDetail(projectId);
        if (projectDetailResponse == null) {
            // TODO: 로깅 및 Exception 던지기
            log.error("ProjectDetailResponse is null");
        }
        return projectDetailResponse;
    }
}
