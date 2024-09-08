package site.hesil.latteve_spring.domains.project.dto.project.request;

/**
 * packageName    : site.hesil.latteve_spring.domains.project.dto.project.request
 * fileName       : updateAcceptStatusRequest
 * author         : Yeong-Huns
 * date           : 2024-09-08
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-08        Yeong-Huns       최초 생성
 */

public record UpdateAcceptStatusRequest(
        long projectId,
        long memberId,
        int acceptStatus
) {
}
