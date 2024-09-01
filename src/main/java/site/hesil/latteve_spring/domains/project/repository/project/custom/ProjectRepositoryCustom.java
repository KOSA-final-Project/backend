package site.hesil.latteve_spring.domains.project.repository.project.custom;

import site.hesil.latteve_spring.domains.project.domain.Project;
import site.hesil.latteve_spring.domains.project.dto.project.response.ProjectDetailResponse;

import java.util.List;

/**
 * packageName    : site.hesil.latteve_spring.domains.project.repository.custom
 * fileName       : ProjectRepositoryCustom
 * author         : JooYoon
 * date           : 2024-08-27
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-27        JooYoon       최초 생성
 */

public interface ProjectRepositoryCustom {
    ProjectDetailResponse getProjectDetail(Long projectId);

    List<Project> findProjectsByMemberIdAndStatus(Long memberId, int status);

    int countProjectsByMemberIdAndStatus(Long memberId, int status);
}
