package site.hesil.latteve_spring.domains.project.repository.project;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.hesil.latteve_spring.domains.project.domain.Project;
import site.hesil.latteve_spring.domains.project.repository.project.custom.ProjectRepositoryCustom;
import site.hesil.latteve_spring.domains.projectStack.domain.ProjectStack;

import java.util.List;

/**
 * packageName    : site.hesil.latteve_spring.domains.project.repository
 * fileName       : ProjectRepository
 * author         : JooYoon
 * date           : 2024-08-27
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-27        JooYoon       최초 생성
 * 2024
 */

public interface ProjectRepository extends JpaRepository<Project, Long>, ProjectRepositoryCustom {
    @Query("SELECT p FROM ProjectLike pl JOIN pl.project p WHERE pl.member.memberId = :memberId")
    Page<Project> findLikedProjectsByMemberId(@Param("memberId") Long memberId, Pageable pageable);


    Page<Project> findProjectsByMemberIdAndStatus(Long memberId, int status, Pageable pageable);

    // 모집중인 프로젝트를 조회할때는 사용자가 리더인 경우만 조회됨
    @Query("SELECT p FROM Project p " +
            "JOIN ProjectMember pm ON p = pm.project " +
            "WHERE pm.member.memberId = :memberId " +
            "AND pm.isLeader = true " +
            "AND p.status = :status")
    Page<Project> findLeaderProjectsByMemberIdAndStatus(@Param("memberId") Long memberId,
                                                        @Param("status") Integer status,
                                                        Pageable pageable);
}