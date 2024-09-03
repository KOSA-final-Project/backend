package site.hesil.latteve_spring.domains.project.repository.projectMember;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.hesil.latteve_spring.domains.project.domain.projectMember.ProjectMember;
import site.hesil.latteve_spring.domains.project.domain.projectMember.ProjectMemberId;

import java.util.List;

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
 */
@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberId> {

    @Query("SELECT COUNT(pm) FROM ProjectMember pm WHERE pm.project.projectId = :projectId")
    Integer findMemberCountByProject_ProjectId(@Param("projectId") Long projectId);

//    @Query("SELECT COUNT(pm) FROM ProjectMember pm WHERE pm.member.memberId = :memberId")
//    Long countByMember_MemberId(@Param("memberId") Long memberId);

    @Query("SELECT pm.project.projectId FROM ProjectMember pm WHERE pm.member.memberId = :memberId")
    List<Long> findProjectIdsByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT pm FROM ProjectMember pm WHERE pm.projectMemberId.projectId = :projectId")
    List<ProjectMember> findByProjectId(@Param("projectId") Long projectId);

    @Modifying
    @Query(value = "INSERT INTO project_member (project_id, member_id, job_id, is_leader, accept_status) VALUES (:projectId, :memberId, 1, 1, 1)", nativeQuery = true)
    void registerProjectLeader(@Param("projectId") Long projectId, @Param("memberId") Long memberId);
}

