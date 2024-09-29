package site.hesil.latteve_spring.domains.alarm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

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

//    private final AlarmRepository alarmRepository;
//
//    // 알람 목록 조회
//    @Transactional(readOnly = true)
//    public AlarmsResponse getNotifications(Long memberId) {
//
//        List<RequestAlarm> requestAlarms = alarmRepository.findUnreadRequestAlarms(memberId);
//        List<ApplicationResultAlarm> applicationResultAlarms = alarmRepository.findUnreadResponseAlarms(memberId);
//
//        return new AlarmsResponse(requestAlarms, applicationResultAlarms);
//    }
//
//    // 알람 읽음 처리
//    public void readNotification(Long alarmId) {
//        alarmRepository.findById(alarmId).ifPresent(alarm -> {
//            alarm.read();
//            alarmRepository.save(alarm);
//        });
//    }
}
