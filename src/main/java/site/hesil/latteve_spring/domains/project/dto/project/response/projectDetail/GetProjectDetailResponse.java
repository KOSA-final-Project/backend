package site.hesil.latteve_spring.domains.project.dto.project.response.projectDetail;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * packageName    : site.hesil.latteve_spring.domains.project.dto.project.response.projectDetail
 * fileName       : ProjectDetailResponse
 * author         : Yeong-Huns
 * date           : 2024-09-11
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-11        Yeong-Huns       최초 생성
 */
@Builder
public record GetProjectDetailResponse(
        Long projectId,
        String name,
        String description,
        String projectImg,
        int status,
        LocalDateTime createdAt,
        LocalDateTime startedAt,
        int duration,
        int cycle,
        List<ProjectDetailTechStack> projectTechStack,
        ProjectDetailLeader leader,
        List<ProjectDetailRecruitment> recruitments
) {
    public static GetProjectDetailResponse of(
            ProjectDetail projectDetail,
            List<ProjectDetailTechStack> projectTechStack,
            ProjectDetailLeader leader,
            List<ProjectDetailRecruitment> recruitments
    ){
        return GetProjectDetailResponse.builder()
                .projectId(projectDetail.projectId())
                .name(projectDetail.name())
                .description(projectDetail.description())
                .projectImg(projectDetail.projectImg())
                .status(projectDetail.status())
                .createdAt(projectDetail.createdAt())
                .startedAt(projectDetail.startedAt())
                .duration(projectDetail.duration())
                .cycle(projectDetail.cycle())
                .projectTechStack(projectTechStack)
                .leader(leader)
                .recruitments(recruitments)
                .build();
    }
}
