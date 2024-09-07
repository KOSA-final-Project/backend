package site.hesil.latteve_spring.domains.project.dto.project.response;

import site.hesil.latteve_spring.domains.memberStack.dto.response.MemberStackResponse;

import java.util.List;

/**
 * packageName    : site.hesil.latteve_spring.domains.project.dto.project.response
 * fileName       : ProjectMemberResponse
 * author         : Yeong-Huns
 * date           : 2024-09-08
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-08        Yeong-Huns       최초 생성
 */
public record ProjectMemberResponse(
        String jobName,
        long projectMemberId,
        String projectMemberNickname,
        String projectMemberGitHub,
        String projectMemberImgUrl,
        long ongoingProjectCount,
        long completedProjectCount
) {
}
