package site.hesil.latteve_spring.domains.alarm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.hesil.latteve_spring.domains.alarm.domain.Alarm;

/**
 * packageName    : site.hesil.latteve_spring.domains.alarm.domain
 * fileName       : AlarmRepository
 * author         : Yeong-Huns
 * date           : 2024-09-02
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-02        Yeong-Huns       최초 생성
 */
@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {
}
