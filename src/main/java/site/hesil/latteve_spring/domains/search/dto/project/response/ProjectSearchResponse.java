package site.hesil.latteve_spring.domains.search.dto.project.response;

import lombok.Builder;
import site.hesil.latteve_spring.domains.project.dto.project.response.ProjectCardResponse;

import java.util.List;

/**
 * packageName    : site.hesil.latteve_spring.domains.search.dto.project.request
 * fileName       : ProjectDocument
 * author         : Heeseon
 * date           : 2024-08-28
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-28        Heeseon       최초 생성
 */
@Builder
public record ProjectSearchResponse(
        Long projectId,
        String name,
        String imgUrl,
        int duration,
        List<ProjectCardResponse.TechStack> projectTechStack,
        Long cntLike, // 좋아요 수
        int currentCnt, // 현재 모인 팀원 수
        int teamCnt, // 모집 인원
        String status,
        String createdAt
){
}
