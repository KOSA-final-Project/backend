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
        int status,
        LocalDate createdAt,
        LocalDate startedAt,
        LocalDate updatedAt,
        int duration,
        int cycle,
        List<TechStack> projectTechStack,
        Leader leader,
        List<Recruitment> recruitments
) {
    public static ProjectDetailResponse of(
            ProjectDetailResponse projectDetail,
            List<TechStack> projectTechStack,
            Leader leader,
            List<Recruitment> recruitments
    ){
        return ProjectDetailResponse.builder()
                .projectId(projectDetail.projectId)
                .name(projectDetail.name)
                .description(projectDetail.description)
                .projectImg(projectDetail.projectImg)
                .status(projectDetail.status)
                .createdAt(projectDetail.createdAt)
                .startedAt(projectDetail.startedAt)
                .duration(projectDetail.duration)
                .cycle(projectDetail.cycle)
                .projectTechStack(projectTechStack)
                .leader(leader)
                .recruitments(recruitments)
                .build();
    }

    public record Leader(
            Long memberId,
            String memberNickname,
            String memberImg,
            String memberGithub,
            String career,
            int ongoingProjectCount,
            int completedProjectCount,
            List<TechStack> techStack
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
            String memberGithub,
            String career,
            int ongoingProjectCount,
            int completedProjectCount,
            List<TechStack> techStack
    ) {}

    public record TechStack(
            String name,
            String imgUrl
    ) {}
}
