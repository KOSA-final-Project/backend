package site.hesil.latteve_spring.domains.alarm.dto;

import site.hesil.latteve_spring.domains.alarm.domain.Alarm;
import site.hesil.latteve_spring.global.error.exception.NotFoundException;

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
public record AlarmExampleResponse(
        long projectId,
        String message,
        boolean isRead
) {
    public static AlarmExampleResponse fromAlarm(Alarm alarm) {
        String type = switch (alarm.getType()){
            case 0 -> "요청";
            case 1 -> "승인";
            case 2 -> "거절";
            default -> throw new NotFoundException();
        };
        // 기타 등등
        return new AlarmExampleResponse(1,type + "메세지" , alarm.isRead());
    }
}
