package site.hesil.latteve_spring.domains.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.hesil.latteve_spring.domains.project.dto.project.response.ProjectDetailResponse;
import site.hesil.latteve_spring.domains.project.repository.project.ProjectRepository;
import site.hesil.latteve_spring.global.error.errorcode.ErrorCode;
import site.hesil.latteve_spring.global.error.exception.CustomBaseException;

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

    private final ProjectRepository projectRepository;

    // 프로젝트 상세 페이지 정보
    public ProjectDetailResponse getProjectDetail(Long projectId) {
        ProjectDetailResponse projectDetailResponse = projectRepository.getProjectDetail(projectId);
        if (projectDetailResponse == null) {
            throw new CustomBaseException(ErrorCode.NOT_FOUND);
        }
        return projectDetailResponse;
    }

    // 프로젝트 지원
    public void applyProject(Long projectId, Long memberId, Long jobId) {

    }
}
