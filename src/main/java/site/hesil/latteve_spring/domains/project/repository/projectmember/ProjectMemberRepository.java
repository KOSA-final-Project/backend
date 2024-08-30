package site.hesil.latteve_spring.domains.project.repository.projectmember;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.hesil.latteve_spring.domains.project.domain.projectMember.ProjectMember;
import site.hesil.latteve_spring.domains.project.domain.projectMember.ProjectMemberId;

/**
 * packageName    : site.hesil.latteve_spring.domains.project.repository
 * fileName       : ProjectMemberRepository
 * author         : Heeseon
 * date           : 2024-08-28
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-28        Heeseon       최초 생성
 */

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberId> {

    @Query("SELECT COUNT(pm) FROM ProjectMember pm WHERE pm.project.projectId = :projectId")
    Integer findMemberCountByProject_ProjectId(@Param("projectId") Long projectId);
}