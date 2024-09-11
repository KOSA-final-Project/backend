package site.hesil.latteve_spring.domains.project.dto.response;

import lombok.Builder;
import site.hesil.latteve_spring.domains.member.domain.Member;
import site.hesil.latteve_spring.domains.memberStack.dto.response.MemberStackResponse;
import site.hesil.latteve_spring.domains.project.dto.project.response.ProjectMemberResponse;

import java.util.List;

/**
 * packageName    : site.hesil.latteve_spring.domains.project.dto.response
 * fileName       : ApplicationResponse
 * author         : Yeong-Huns
 * date           : 2024-09-03
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-03        Yeong-Huns       최초 생성
 * 2024-09-03        Yeong-Huns       projectMemberId -> projectMemberNickname 수정
 */
@Builder
public record ApplicationResponse(
        long jobId,
        String jobName,
        long projectMemberId,
        String projectMemberNickname,
        String projectMemberGitHub,
        String projectMemberImgUrl,
        long ongoingProjectCount,
        long completedProjectCount,
        List<MemberStackResponse> techStack
) {
    public static ApplicationResponse of(ProjectMemberResponse projectMemberResponse, List<MemberStackResponse> techStack) {
        return ApplicationResponse.builder()
                .jobId(projectMemberResponse.jobId())
                .projectMemberId(projectMemberResponse.projectMemberId())
                .projectMemberNickname(projectMemberResponse.projectMemberNickname())
                .projectMemberGitHub(projectMemberResponse.projectMemberGitHub())
                .projectMemberImgUrl(projectMemberResponse.projectMemberImgUrl())
                .jobName(projectMemberResponse.jobName())
                .ongoingProjectCount(projectMemberResponse.ongoingProjectCount())
                .completedProjectCount(projectMemberResponse.completedProjectCount())
                .techStack(techStack)
                .build();
    }
}
