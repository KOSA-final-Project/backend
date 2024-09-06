package site.hesil.latteve_spring.domains.project.dto.project.request;

import lombok.Builder;

/**
 * packageName    : site.hesil.latteve_spring.domains.project.dto.project.request
 * fileName       : ProjectApplyRequest
 * author         : JooYoon
 * date           : 2024-09-01
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-01        JooYoon       최초 생성
 */
@Builder
public record ProjectApplyRequest(Long jobId) {}
