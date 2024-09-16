package site.hesil.latteve_spring.domains.alarm.dto;

import java.util.List;

/**
 * packageName    : site.hesil.latteve_spring.domains.alarm.dto
 * fileName       : AlarmExampleResponse
 * author         : Yeong-Huns
 * date           : 2024-09-09
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-09        Yeong-Huns       최초 생성
 */
public record AlarmsResponse(
        List<RequestAlarm> requestAlarms,
        List<ApplicationResultAlarm> applicationResultAlarms
) {
}
