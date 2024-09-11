package site.hesil.latteve_spring.domains.alarm.dto;

import lombok.Builder;
import site.hesil.latteve_spring.domains.project.domain.projectMember.ProjectMember;

/**
 * packageName    : site.hesil.latteve_spring.domains.alarm.dto
 * fileName       : ProjectApprovalResultAlarm
 * author         : JooYoon
 * date           : 2024-09-08
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-08        JooYoon       최초 생성
 */

// 프로젝트 승인 및 거절 알람: 프로젝트명, 승인여부, 받는멤버아이디
@Builder
public record ProjectApprovalResultAlarm(String projectName, int acceptStatus, Long receiverMemberId) {
    public static ProjectApprovalResultAlarm from (ProjectMember projectMember) {
        return ProjectApprovalResultAlarm.builder()
                .projectName(projectMember.getProject().getName())
                .acceptStatus(projectMember.getAcceptStatus())
                .receiverMemberId(projectMember.getMember().getMemberId())
                .build();
    }
}
