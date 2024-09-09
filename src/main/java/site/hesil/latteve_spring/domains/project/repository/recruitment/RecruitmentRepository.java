package site.hesil.latteve_spring.domains.project.repository.recruitment;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import site.hesil.latteve_spring.domains.project.domain.Project;
import site.hesil.latteve_spring.domains.project.domain.recruitment.Recruitment;
import site.hesil.latteve_spring.domains.project.domain.recruitment.RecruitmentId;
import site.hesil.latteve_spring.domains.project.dto.request.projectSave.RecruitmentRoles;

import java.util.List;
import java.util.Optional;

/**
 * packageName    : site.hesil.latteve_spring.domains.project.repository.recruitment
 * fileName       : RecruitmentRepostory
 * author         : Heeseon
 * date           : 2024-08-28
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-28        Heeseon       최초 생성
 * 2024-09-01        Yeong-Huns    프로젝트 생성 커스텀 로직 추가
 * 2024-09-08        Heeseon       프로젝트로 직무 조회 추가, 직무 개수 조회 추가
 */
public interface RecruitmentRepository extends JpaRepository <Recruitment, RecruitmentId> {

    @Query("SELECT COUNT(r) FROM Recruitment r WHERE r.project.projectId = :projectId")
    Integer findMemberCountByProject_ProjectId(@Param("projectId") Long projectId);

    @Modifying
    @Query(value = "INSERT INTO recruitment (project_id, job_id, count) VALUES (:projectId, :jobId, :count)", nativeQuery = true)
    void saveRecruitment(@Param("projectId") Long projectId, @Param("jobId") Long jobId, @Param("count") int count);


    default void saveAllRecruitments(List<RecruitmentRoles> roles, Long projectId) {
        roles.forEach(i->saveRecruitment(projectId, i.jobId(), i.count()));
    }

    @Query("SELECT r FROM Recruitment r WHERE r.project = :project AND r.job.jobId <> 1")
    List<Recruitment> findByProjectExcludingJobId1(@Param("project") Project project);


}
