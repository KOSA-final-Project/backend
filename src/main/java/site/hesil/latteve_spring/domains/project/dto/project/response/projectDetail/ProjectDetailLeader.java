package site.hesil.latteve_spring.domains.project.dto.project.response.projectDetail;

import java.util.List;

/**
 * packageName    : site.hesil.latteve_spring.domains.project.dto.project.response.projectDetail
 * fileName       : ProjectDetailLeader
 * author         : Yeong-Huns
 * date           : 2024-09-11
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-11        Yeong-Huns       최초 생성
 */
public record ProjectDetailLeader(
        Long memberId,
        String memberNickname,
        String memberImg,
        String memberGithub,
        int ongoingProjectCount,
        int completedProjectCount,
        List<ProjectDetailTechStack> techStack
) {
}
