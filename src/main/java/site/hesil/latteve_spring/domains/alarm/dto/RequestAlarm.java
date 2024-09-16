package site.hesil.latteve_spring.domains.alarm.dto;

/**
 * packageName    : site.hesil.latteve_spring.domains.alarm.dto
 * fileName       : AlarmInfo
 * author         : JooYoon
 * date           : 2024-09-16
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-16        JooYoon       최초 생성
 */

public record RequestAlarm(
        Long projectId,
        String projectName,
        String requesterNickname
) {
}
