package site.hesil.latteve_spring.domains.search.dto.project.request;

import lombok.Builder;
import site.hesil.latteve_spring.domains.search.dto.project.response.ProjectSearchResponse;

import java.util.List;

/**
 * packageName    : site.hesil.latteve_spring.domains.search.dto.project.request
 * fileName       : ProjectDoc
 * author         : Heeseon
 * date           : 2024-09-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-13        Heeseon       최초 생성
 */
@Builder
public record ProjectDocReq(
        Long projectId,
        String name,
        String imgUrl,
        int duration,
        List<TechStack> projectTechStack,
        String status,
        String createdAt,
        int teamCnt
) {
    public record TechStack(
            String name,
            String imgUrl
    ){}
}
