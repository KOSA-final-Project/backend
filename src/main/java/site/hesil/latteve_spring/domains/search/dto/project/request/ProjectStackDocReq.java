package site.hesil.latteve_spring.domains.search.dto.project.request;

import lombok.Builder;

import java.util.List;

/**
 * packageName    : site.hesil.latteve_spring.domains.search.dto.project.request
 * fileName       : ProjectStatckDocReq
 * author         : Heeseon
 * date           : 2024-09-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-13        Heeseon       최초 생성
 */
@Builder
public record ProjectStackDocReq(
        Long projectId,
        List<TechStack> techStackList
) {
    public record TechStack(
            String name,
            String imgUrl
    ){}
}
