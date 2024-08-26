package site.hesil.latteve_spring.domains.project.dto.project.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * packageName    : site.hesil.latteve_spring.domains.project.dto.project.response
 * fileName       : ProjectDetailResponse
 * author         : JooYoon
 * date           : 2024-08-26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-26        JooYoon       최초 생성
 */

@Builder
public record ProjectDetailResponse(
        Long projectId,
        String title,
        String description,
        String projectImg,
        Map<String, Object> leader,
        Map<String, Object> members,
        Map<String, Object> techStack,
        int status,
        LocalDateTime createdAt,
        int duration,
        int cycle
) {
}
