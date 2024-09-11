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
import java.util.Optional;

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

    // 신규순으로 조회하는 쿼리
    @Query("SELECT p FROM Project p WHERE p.status <> 2 ORDER BY p.createdAt DESC")
    Page<Project> findAllByStatusOrderByCreatedAtDesc(int status, Pageable pageable);


    // 종료된 프로젝트 조회
    @Query("SELECT p FROM Project p WHERE p.status = 2 AND p.deletedAt IS NULL AND p.startedAt IS NOT NULL")
    Page<Project> findAllCompletedProjects(Pageable pageable);

    /*// ProjectDetail ===================================
    @Query("""
        SELECT new site.hesil.latteve_spring.domains.project.dto.project.response.ProjectDetailResponse(
            p.projectId, p.name, p.description, p.imgUrl, p.status,
            p.createdAt, p.startedAt, p.duration, p.cycle,
            null, null, null
        )
        FROM Project p
        WHERE p.projectId = :projectId
    """)
    Optional<ProjectDetailResponse> findProjectDetailById(@Param("projectId") Long projectId);

    @Query("""
        SELECT new site.hesil.latteve_spring.domains.project.dto.project.response.ProjectDetailResponse.TechStack(
            CASE WHEN ps.techStack.techStackId = 1 THEN ps.customStack ELSE ts.name END, ts.imgUrl
        )
        FROM ProjectStack ps
        LEFT JOIN ps.techStack ts
        WHERE ps.project.projectId = :projectId
    """)
    List<ProjectDetailResponse.TechStack> findTechStacksByProjectId(@Param("projectId") Long projectId);

    @Query("""
        SELECT new site.hesil.latteve_spring.domains.project.dto.project.response.ProjectDetailResponse.Leader(
            pm.member.memberId, pm.member.nickname, pm.member.imgUrl, pm.member.github,
            SUM(CASE WHEN pm.project.status = 1 THEN 1 ELSE 0 END),
            SUM(CASE WHEN pm.project.status = 2 THEN 1 ELSE 0 END),
            (SELECT new site.hesil.latteve_spring.domains.project.dto.project.response.ProjectDetailResponse.TechStack(
                CASE WHEN ms.techStack.techStackId = 1 THEN ms.customStack ELSE ts.name END, ts.imgUrl
            ) FROM MemberStack ms LEFT JOIN ms.techStack ts WHERE ms.member.memberId = pm.member.memberId)
        )
        FROM ProjectMember pm
        WHERE pm.isLeader = true AND pm.project.projectId = :projectId
        GROUP BY pm.member.memberId, pm.member.nickname, pm.member.imgUrl, pm.member.github
    """)
    Optional<ProjectDetailResponse.Leader> findLeaderByProjectId(@Param("projectId") Long projectId);

    @Query("""
        SELECT new site.hesil.latteve_spring.domains.project.dto.project.response.ProjectDetailResponse.Recruitment(
            r.job.jobId, j.name, r.count,
            (SELECT new site.hesil.latteve_spring.domains.project.dto.project.response.ProjectDetailResponse.Member(
                pm.member.memberId, pm.member.nickname, pm.member.imgUrl, pm.member.github,
                SUM(CASE WHEN pm.project.status = 1 THEN 1 ELSE 0 END),
                SUM(CASE WHEN pm.project.status = 2 THEN 1 ELSE 0 END),
                (SELECT new site.hesil.latteve_spring.domains.project.dto.project.response.ProjectDetailResponse.TechStack(
                    CASE WHEN ms.techStack.techStackId = 1 THEN ms.customStack ELSE ts.name END, ts.imgUrl
                ) FROM MemberStack ms LEFT JOIN ms.techStack ts WHERE ms.member.memberId = pm.member.memberId)
            ) FROM ProjectMember pm WHERE pm.project.projectId = :projectId AND pm.job.jobId = r.job.jobId AND pm.acceptStatus = 1)
        )
        FROM Recruitment r
        JOIN r.job j
        WHERE r.project.projectId = :projectId AND r.job.jobId <> 1
    """)
    List<ProjectDetailResponse.Recruitment> findRecruitmentsByProjectId(@Param("projectId") Long projectId);*/

}
