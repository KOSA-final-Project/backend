package site.hesil.latteve_spring.domains.alarm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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
    @Query("""
    SELECT a
    FROM Alarm a
    WHERE a.project.projectId = :projectId AND a.member.memberId = :memberId And a.type = 0
""")
    Optional<Alarm> findAlarmByProjectIdAndMemberId(@Param("projectId") long projectId, @Param("memberId") long memberId);


    @Modifying
    @Query("UPDATE Alarm a SET a.type = 2 WHERE a.project.projectId = :projectId AND a.type = 0")
    void updateTypeByProjectId(@Param("projectId") Long projectId);

    @Query("""
            select new site.hesil.latteve_spring.domains.alarm.dto.RequestAlarm(p.projectId, p.name, m.nickname)
            from Alarm a
            join a.project p
            join a.member m
            join ProjectMember pm on pm.project.projectId = p.projectId
            where pm.member.memberId = :memberId and pm.isLeader = true
            and a.type = 0 and a.isRead = false
            """)
    List<RequestAlarm> findUnreadRequestAlarms(@Param("memberId") Long memberId);

    @Query("""
            select new site.hesil.latteve_spring.domains.alarm.dto.ApplicationResultAlarm(a.type, a.project.projectId, a.project.name)
            from Alarm a
            join a.project p
            where a.member.memberId = :memberId and a.isRead = false and a.type in (1, 2)
            """)
    List<ApplicationResultAlarm> findUnreadResponseAlarms(@Param("memberId") Long memberId);
}
