package site.hesil.latteve_spring.domains.search.dto.project.request;

import lombok.Builder;
import site.hesil.latteve_spring.domains.techStack.domain.TechStack;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
public record ProjectDocumentReq (
        Long projectId,
        String name,
        String imgUrl,
        int duration,
        List<TechStack> projectTechStack,
        Long like, // 좋아요 수
        int currentCnt, // 현재 모인 팀원 수
        int teamCnt, // 모집 인원
        String status,
        Long cnt,
        String createdAt
){
    public record TechStack(
            String name,
            String imgUrl
    ){}
}
