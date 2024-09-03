package site.hesil.latteve_spring.domains.project.dto.response;

import lombok.Builder;

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
 */
@Builder
public record ApplicationResponse(
        long projectMemberId,
        String jobName,
        List<String> techStack
) {
    public static ApplicationResponse of(long projectMemberId, String jobName, List<String> techStack) {
        return ApplicationResponse.builder()
                .projectMemberId(projectMemberId)
                .jobName(jobName)
                .techStack(techStack)
                .build();
    }
}
