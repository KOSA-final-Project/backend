package site.hesil.latteve_spring.domains.project.repository.recruitment;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import site.hesil.latteve_spring.domains.project.domain.recruitment.Recruitment;
import site.hesil.latteve_spring.domains.project.domain.recruitment.RecruitmentId;
import site.hesil.latteve_spring.domains.project.dto.request.projectSave.RecruitmentRoles;

import java.util.List;

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
 */
public interface RecruitmentRepository extends JpaRepository <Recruitment, RecruitmentId> {

    @Query("SELECT COUNT(r) FROM Recruitment r WHERE r.project.projectId = :projectId")
    Integer findMemberCountByProject_ProjectId(@Param("projectId") Long projectId);

    @Modifying
    @Query(value = "INSERT INTO recruitment (project_id, job_id, count) VALUES (:projectId, :jobId, :count)", nativeQuery = true)
    void saveRecruitment(@Param("projectId") Long projectId, @Param("jobId") Long jobId, @Param("count") int count);


    default void saveAllRecruitments(List<RecruitmentRoles> roles, Long projectId) {
        for (RecruitmentRoles role : roles) {
            saveRecruitment(projectId, role.jobId(), role.count());
        }
    }

}
