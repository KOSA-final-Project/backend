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
 * 2024-09-01        Yeong-Huns    프로젝트 생성 커스텀 로직 추가
 * 2024-09-08        Heeseon       프로젝트로 직무 조회 추가, 직무 개수 조회 추가
 */
public interface RecruitmentRepository extends JpaRepository <Recruitment, Long> {


}
