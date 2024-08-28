package site.hesil.latteve_spring.domains.project.repository.recruitment;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.hesil.latteve_spring.domains.project.domain.recruitment.Recruitment;

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
 */
public interface RecruitmentRepository extends JpaRepository <Recruitment, Long> {

    @Query("SELECT COUNT(r) FROM Recruitment r WHERE r.project.projectId = :projectId")
    Integer findMemberCountByProject_ProjectId(@Param("projectId") Long projectId);
}
