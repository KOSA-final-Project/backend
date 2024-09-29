package site.hesil.latteve_spring.domains.alarm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.hesil.latteve_spring.domains.alarm.domain.Alarm;
import site.hesil.latteve_spring.domains.alarm.dto.ApplicationResultAlarm;
import site.hesil.latteve_spring.domains.alarm.dto.RequestAlarm;

import java.util.List;
import java.util.Optional;

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
//
//    Optional<Alarm> findAlarmByProjectIdAndMemberId(@Param("projectId") long projectId, @Param("memberId") long memberId);
//
//    void updateTypeByProjectId(@Param("projectId") Long projectId);
//
//    List<RequestAlarm> findUnreadRequestAlarms(@Param("memberId") Long memberId);
//
//    List<ApplicationResultAlarm> findUnreadResponseAlarms(@Param("memberId") Long memberId);
}
