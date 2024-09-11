package site.hesil.latteve_spring.domains.project.repository.project.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.hesil.latteve_spring.domains.project.domain.Project;
import site.hesil.latteve_spring.domains.project.dto.project.response.PopularProjectResponse;
import site.hesil.latteve_spring.domains.project.dto.project.response.ProjectDetailResponse;
import site.hesil.latteve_spring.domains.project.dto.response.RetrospectiveResponse;

import java.util.List;
import java.util.Optional;

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
 * 2024-09-01        Heeseon       멤버, 상태로 프로젝트 수 조회
 * 2024-09-04        Heeseon       멤버, 상태로 프로젝트 조회
 * 2024-09-11        Heeseon       인기 프로젝트 조회
 */

public interface ProjectRepositoryCustom {

    Optional<ProjectDetailResponse> getProjectDetail(Long projectId);

    int countProjectsByMemberIdAndStatus(Long memberId, int status);

    Page<Project> findProjectsByMemberIdAndStatus(Long memberId, int status, Pageable pageable);

    Optional<RetrospectiveResponse> getRetrospective(Long projectId, Long memberId, int week);

    List<PopularProjectResponse> findPopularProjects(int limit );
}
