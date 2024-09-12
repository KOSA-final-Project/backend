package site.hesil.latteve_spring.domains.project.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

/**
 * packageName    : site.hesil.latteve_spring.domains.project.dto.response
 * fileName       : RetrospectiveResponse
 * author         : JooYoon
 * date           : 2024-09-05
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-05        JooYoon       최초 생성
 */

@Builder
public record RetrospectiveResponse(Long retId, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
}
