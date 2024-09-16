package site.hesil.latteve_spring.domains.alarm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hesil.latteve_spring.domains.alarm.dto.AlarmsResponse;
import site.hesil.latteve_spring.domains.alarm.dto.ApplicationResultAlarm;
import site.hesil.latteve_spring.domains.alarm.dto.RequestAlarm;
import site.hesil.latteve_spring.domains.alarm.repository.AlarmRepository;

import java.util.List;

/**
 * packageName    : site.hesil.latteve_spring.domains.alarm.service
 * fileName       : AlarmService
 * author         : Yeong-Huns
 * date           : 2024-09-09
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-09        Yeong-Huns       최초 생성
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;

    @Transactional(readOnly = true)
    public AlarmsResponse getNotifications(Long memberId) {

        List<RequestAlarm> requestAlarms = alarmRepository.findUnreadRequestAlarms(memberId);
        List<ApplicationResultAlarm> applicationResultAlarms = alarmRepository.findUnreadResponseAlarms(memberId);

        return new AlarmsResponse(requestAlarms, applicationResultAlarms);
    }
}
