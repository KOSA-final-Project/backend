package site.hesil.latteve_spring.domains.project.dto.project.response.projectDetail;

/**
 * packageName    : site.hesil.latteve_spring.domains.project.dto.project.response.projectDetail
 * fileName       : ProjectDetailLeaderWithoutTechStacks
 * author         : Yeong-Huns
 * date           : 2024-09-11
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-11        Yeong-Huns       최초 생성
 */
public record ProjectDetailLeaderWithoutTechStacks(
        Long memberId,
        String memberNickname,
        String memberImg,
        String memberGithub,
        int ongoingProjectCount,
        int completedProjectCount
) {
}
