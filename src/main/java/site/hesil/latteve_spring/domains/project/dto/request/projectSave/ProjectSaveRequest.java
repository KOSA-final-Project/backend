package site.hesil.latteve_spring.domains.project.dto.request.projectSave;

import lombok.Builder;
import site.hesil.latteve_spring.domains.project.domain.Project;

import java.util.List;

/**
 * packageName    : site.hesil.latteve_spring.domains.project.dto.request
 * fileName       : ProjectSaveRequest
 * author         : Yeong-Huns
 * date           : 2024-09-02
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-02        Yeong-Huns       최초 생성
 */

@Builder
public record ProjectSaveRequest(
        String projectName,
        String projectImage,
        List<RecruitmentRoles> recruitmentRoles,
        String content,
        List<TechStack> techStack,
        int duration,
        int cycle
) {
    public Project toEntity(){
        return Project.builder()
                .name(projectName)
                .description(content)
                .imgUrl(projectImage)
                .status(0)
                .duration(duration)
                .cycle(cycle)
                .build();
    }


}

