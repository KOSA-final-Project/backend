package site.hesil.latteve_spring.domains.project.repository.project;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hesil.latteve_spring.domains.project.domain.Project;
import site.hesil.latteve_spring.domains.project.repository.project.custom.ProjectRepositoryCustom;

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

//    Page<Project> findLikedProjectsByMemberId(@Param("memberId") Long memberId, Pageable pageable);
//
//    Page<Project> findProjectsByMemberIdAndStatus(Long memberId, int status, Pageable pageable);
//
//    Page<Project> findLeaderProjectsByMemberIdAndStatus(@Param("memberId") Long memberId,
//                                                        @Param("status") Integer status,
//                                                        Pageable pageable);
//
//    // 신규순으로 조회하는 쿼리
//    Page<Project> findAllByStatusOrderByCreatedAtDesc(int status, Pageable pageable);
//
//
//    // 종료된 프로젝트 조회
//    Page<Project> findAllCompletedProjects(Pageable pageable);
//
//    /*// ProjectDetail ===================================
//    @Query("""
//        SELECT new site.hesil.latteve_spring.domains.project.dto.project.response.ProjectDetailResponse(
//            p.projectId, p.name, p.description, p.imgUrl, p.status,
//            p.createdAt, p.startedAt, p.duration, p.cycle,
//            null, null, null
//        )
//        FROM Project p
//        WHERE p.projectId = :projectId
//    """)
//    Optional<ProjectDetailResponse> findProjectDetailById(@Param("projectId") Long projectId);
//
//    @Query("""
//        SELECT new site.hesil.latteve_spring.domains.project.dto.project.response.ProjectDetailResponse.TechStack(
//            CASE WHEN ps.techStack.techStackId = 1 THEN ps.customStack ELSE ts.name END, ts.imgUrl
//        )
//        FROM ProjectStack ps
//        LEFT JOIN ps.techStack ts
//        WHERE ps.project.projectId = :projectId
//    """)
//    List<ProjectDetailResponse.TechStack> findTechStacksByProjectId(@Param("projectId") Long projectId);
//
//    @Query("""
//        SELECT new site.hesil.latteve_spring.domains.project.dto.project.response.ProjectDetailResponse.Leader(
//            pm.member.memberId, pm.member.nickname, pm.member.imgUrl, pm.member.github,
//            SUM(CASE WHEN pm.project.status = 1 THEN 1 ELSE 0 END),
//            SUM(CASE WHEN pm.project.status = 2 THEN 1 ELSE 0 END),
//            (SELECT new site.hesil.latteve_spring.domains.project.dto.project.response.ProjectDetailResponse.TechStack(
//                CASE WHEN ms.techStack.techStackId = 1 THEN ms.customStack ELSE ts.name END, ts.imgUrl
//            ) FROM MemberStack ms LEFT JOIN ms.techStack ts WHERE ms.member.memberId = pm.member.memberId)
//        )
//        FROM ProjectMember pm
//        WHERE pm.isLeader = true AND pm.project.projectId = :projectId
//        GROUP BY pm.member.memberId, pm.member.nickname, pm.member.imgUrl, pm.member.github
//    """)
//    Optional<ProjectDetailResponse.Leader> findLeaderByProjectId(@Param("projectId") Long projectId);
//
//    @Query("""
//        SELECT new site.hesil.latteve_spring.domains.project.dto.project.response.ProjectDetailResponse.Recruitment(
//            r.job.jobId, j.name, r.count,
//            (SELECT new site.hesil.latteve_spring.domains.project.dto.project.response.ProjectDetailResponse.Member(
//                pm.member.memberId, pm.member.nickname, pm.member.imgUrl, pm.member.github,
//                SUM(CASE WHEN pm.project.status = 1 THEN 1 ELSE 0 END),
//                SUM(CASE WHEN pm.project.status = 2 THEN 1 ELSE 0 END),
//                (SELECT new site.hesil.latteve_spring.domains.project.dto.project.response.ProjectDetailResponse.TechStack(
//                    CASE WHEN ms.techStack.techStackId = 1 THEN ms.customStack ELSE ts.name END, ts.imgUrl
//                ) FROM MemberStack ms LEFT JOIN ms.techStack ts WHERE ms.member.memberId = pm.member.memberId)
//            ) FROM ProjectMember pm WHERE pm.project.projectId = :projectId AND pm.job.jobId = r.job.jobId AND pm.acceptStatus = 1)
//        )
//        FROM Recruitment r
//        JOIN r.job j
//        WHERE r.project.projectId = :projectId AND r.job.jobId <> 1
//    """)
//    List<ProjectDetailResponse.Recruitment> findRecruitmentsByProjectId(@Param("projectId") Long projectId);*/

}
