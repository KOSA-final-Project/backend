package site.hesil.latteve_spring.domains.project.dto.project.request;

import lombok.Builder;

/**
 * packageName    : site.hesil.latteve_spring.domains.project.dto.project.request
 * fileName       : AcceptedProjectMember
 * author         : Heeseon
 * date           : 2024-09-16
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-16        Heeseon       최초 생성
 */
@Builder
public record AcceptedProjectMemberRequest(
        long projectId,
        long jobId,
        long memberId
) {
}
