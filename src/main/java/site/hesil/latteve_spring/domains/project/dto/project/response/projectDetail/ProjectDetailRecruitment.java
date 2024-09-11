package site.hesil.latteve_spring.domains.project.dto.project.response.projectDetail;

import site.hesil.latteve_spring.domains.project.dto.project.response.ProjectDetailResponse;

import java.util.List;

/**
 * packageName    : site.hesil.latteve_spring.domains.project.dto.project.response.projectDetail
 * fileName       : ProjectDetailRecruitment
 * author         : Yeong-Huns
 * date           : 2024-09-11
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-11        Yeong-Huns       최초 생성
 */
public record ProjectDetailRecruitment(
        Long jobId,
        String jobName,
        int jobCount,
        List<ProjectDetailMember> members
) {
}
