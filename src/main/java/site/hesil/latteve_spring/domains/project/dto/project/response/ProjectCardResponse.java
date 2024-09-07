package site.hesil.latteve_spring.domains.project.dto.project.response;


import lombok.Builder;

import java.util.List;

/**
 * packageName    : site.hesil.latteve_spring.domains.project.dto.project.response
 * fileName       : ProjectCardResponse
 * author         : Heeseon
 * date           : 2024-09-04
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-04        Heeseon       최초 생성
 */
@Builder
public record ProjectCardResponse(
        Long projectId,
        String name,
        String imgUrl,
        int duration,
        List<TechStack> projectTechStack,
        boolean isLiked, // 내가 '좋아요' 눌렀는지
        Long cntLike, // 좋아요 수
        int currentCnt, // 현재 모인 팀원 수
        int teamCnt // 모집 인원
) {
    public record TechStack(
            String name,
            String imgUrl
    ){}
}
