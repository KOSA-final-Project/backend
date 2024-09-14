package site.hesil.latteve_spring.domains.alarm.dto;

import lombok.Builder;

/**
 * packageName    : site.hesil.latteve_spring.domains.alarm.dto
 * fileName       : ProjectApplicationAlarm
 * author         : JooYoon
 * date           : 2024-09-08
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-08        JooYoon       최초 생성
 */

// 지원 알람: 프로젝트명, 멤버닉네임, 직무명, 프로젝트리더아이디
@Builder
public record ProjectApplicationAlarm(String projectName,long memberId,String imgUrl, String nickname, String jobName, Long projectLeaderId, String type) {}
