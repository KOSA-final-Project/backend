package site.hesil.latteve_spring.domains.project.repository.project.custom;

import site.hesil.latteve_spring.domains.project.dto.project.response.ProjectDetailResponse;

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
}
