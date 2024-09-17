package site.hesil.latteve_spring.domains.search.dto.project.request;

import lombok.Builder;

/**
 * packageName    : site.hesil.latteve_spring.domains.search.dto.project.request
 * fileName       : ProjectMemberDoc
 * author         : Heeseon
 * date           : 2024-09-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-13        Heeseon       최초 생성
 */
@Builder
public record ProjectMemberDocReq(
        Long projectId,
        int currentMemberCount
//        Long memberId,
//        Long jobId
) {
}
