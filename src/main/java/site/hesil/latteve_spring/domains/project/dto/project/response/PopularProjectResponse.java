package site.hesil.latteve_spring.domains.project.dto.project.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

/**
 *packageName    : site.hesil.latteve_spring.domains.project.dto.project.response
 * fileName       : PopularProjectResponse
 * author         : Heeseon
 * date           : 2024-09-06
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-06        Heeseon       최초 생성
 */
@Builder
public record PopularProjectResponse(
        Long projectId,
        String name,
        String imgUrl,
        List<ProjectCardResponse.TechStack> projectTechStack,
        String description,
        List <String> recruitmentName,
        int duration,
        LocalDateTime createdAt,
        Long cntLike, // 좋아요 수
        int currentCnt, // 현재 모인 팀원 수
        int teamCnt,
        double popularityScore
) {
}
