package site.hesil.latteve_spring.domains.search.dto.project.request;

import lombok.Builder;

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
        String status,
        String createdAt
) {
}
