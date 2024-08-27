package site.hesil.latteve_spring.domains.project.dto.project.response;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

/**
 * packageName    : site.hesil.latteve_spring.domains.project.dto.project.response
 * fileName       : ProjectDetailResponse
 * author         : JooYoon
 * date           : 2024-08-26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-26        JooYoon       최초 생성
 */

@Builder
public record ProjectDetailResponse(
        Long projectId,
        String name,
        String description,
        String projectImg,
        List<String> projectTechStack,
        int status,
        LocalDate createdAt,
        LocalDate startedAt,
        int duration,
        int cycle,
        Leader leader,
        List<Recruitment> recruitments
) {
    public record Leader(
            Long memberId,
            String memberNickname,
            String memberImg,
            List<String> techStack
    ) {}

    public record Recruitment(
            Long jobId,
            String jobName,
            int jobCount,
            List<Member> members
    ) {}

    public record Member(
            Long memberId,
            String memberNickname,
            String memberImg,
            List<String> techStack
    ) {}
}
