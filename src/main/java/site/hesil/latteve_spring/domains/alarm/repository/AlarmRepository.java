package site.hesil.latteve_spring.domains.alarm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hesil.latteve_spring.domains.alarm.domain.Alarm;

/**
 * packageName    : site.hesil.latteve_spring.domains.alarm.repository
 * fileName       : AlarmRepository
 * author         : JooYoon
 * date           : 2024-09-02
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-02        JooYoon       최초 생성
 */
public interface AlarmRepository extends JpaRepository<Alarm, Long> {

}
