package site.hesil.latteve_spring.domains.project.repository.recruitment;

import org.springframework.data.jpa.repository.JpaRepository;
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


    Integer findMemberCountByProject_ProjectId(Long projectId);
}
