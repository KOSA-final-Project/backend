package site.hesil.latteve_spring.domains.project.repository.projectMember;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.hesil.latteve_spring.domains.project.domain.projectMember.ProjectMember;
import site.hesil.latteve_spring.domains.project.domain.projectMember.ProjectMemberId;
import site.hesil.latteve_spring.domains.project.dto.project.response.ProjectMemberResponse;

import java.util.List;
import java.util.Optional;

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
 * 2024-09-02        Yeong-Huns    projectId 를 통한 조회
 * 2024-09-02        Yeong-Huns    Project 생성시에 자동 리더 등록
 * 2024-09-09        Yeong-Huns    프로젝트 ID 를 통해 해당 Project 의 리더 Id 를 찾음
 */
@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberId> {

    @Query("SELECT COUNT(pm) FROM ProjectMember pm WHERE pm.project.projectId = :projectId AND pm.acceptStatus = 1")
    Integer findApprovedMemberCountByProject_ProjectId(@Param("projectId") Long projectId);


    @Query("SELECT COUNT(pm) FROM ProjectMember pm WHERE pm.project.projectId = :projectId")
    Integer findMemberCountByProject_ProjectId(@Param("projectId") Long projectId);


    @Query("SELECT pm.project.projectId FROM ProjectMember pm WHERE pm.member.memberId = :memberId")
    List<Long> findProjectIdsByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT pm FROM ProjectMember pm WHERE pm.projectMemberId.projectId = :projectId and pm.acceptStatus = 2")
    List<ProjectMember> findByProjectIdAndNotAccept(@Param("projectId") Long projectId);

    @Modifying
    @Query(value = "INSERT INTO project_member (project_id, member_id, job_id, is_leader, accept_status) VALUES (:projectId, :memberId, 1, 1, 1)", nativeQuery = true)
    void registerProjectLeader(@Param("projectId") Long projectId, @Param("memberId") Long memberId);

    @Query("""
                SELECT new site.hesil.latteve_spring.domains.project.dto.project.response.ProjectMemberResponse(
                    pm.job.jobId,
                    pm.job.name,
                    pm.member.memberId,
                    pm.member.nickname,
                    pm.member.github,
                    pm.member.imgUrl,
                    (SELECT COUNT(p)
                     FROM ProjectMember pm2
                     JOIN pm2.project p
                     WHERE pm2.member.memberId = pm.member.memberId
                       AND p.status = 1
                       AND pm2.acceptStatus = 1),
                    (SELECT COUNT(p)
                     FROM ProjectMember pm3
                     JOIN pm3.project p
                     WHERE pm3.member.memberId = pm.member.memberId
                       AND p.status = 2
                       AND pm3.acceptStatus = 1)
                )
                FROM ProjectMember pm
                WHERE pm.project.projectId = :projectId
                  AND pm.acceptStatus = 2
            """)
    List<ProjectMemberResponse> findApplicationsByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT pm.isLeader FROM ProjectMember pm WHERE pm.project.projectId = :projectId AND pm.member.memberId = :memberId")
    boolean isLeader(@Param("projectId") Long projectId, @Param("memberId") Long memberId);

    @Query("SELECT COUNT(pm) > 0 FROM ProjectMember pm WHERE pm.project.projectId = :projectId AND pm.member.memberId = :memberId AND (pm.acceptStatus = 1 OR pm.acceptStatus = 2)")
    boolean isApplication(@Param("projectId") Long projectId, @Param("memberId") Long memberId);

    @Query("SELECT pm FROM ProjectMember pm WHERE pm.project.projectId = :projectId AND pm.member.memberId = :memberId")
    Optional<ProjectMember> findByProjectIdAndMemberId(@Param("projectId") Long projectId,  @Param("memberId") Long memberId);

    @Query("SELECT pm FROM ProjectMember pm WHERE pm.project.projectId = :projectId AND pm.member.memberId = :memberId AND pm.job.jobId = :jobId AND pm.acceptStatus = 2")
    Optional<ProjectMember> findByProjectIdAndMemberIdAndJobId(@Param("projectId") Long projectId, @Param("jobId") int jobId , @Param("memberId") Long memberId);

    @Modifying
    @Query("UPDATE ProjectMember pm SET pm.acceptStatus = 0 WHERE pm.project.projectId = :projectId AND pm.acceptStatus = 2")
    void updateAcceptStatusByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT pm.member.memberId FROM ProjectMember pm WHERE pm.project.projectId = :projectId AND pm.isLeader = true")
    Optional<Long> findLeaderMemberIdByProjectId(@Param("projectId") Long projectId);


}

