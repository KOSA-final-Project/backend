package site.hesil.latteve_spring.domains.project.dto.project.response.projectDetail;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * packageName    : site.hesil.latteve_spring.domains.project.dto.project.response.projectDetail
 * fileName       : ProjectDetail
 * author         : Yeong-Huns
 * date           : 2024-09-11
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-11        Yeong-Huns       최초 생성
 */
public record ProjectDetail(
        Long projectId,
        String name,
        String description,
        String projectImg,
        int status,
        LocalDateTime createdAt,
        LocalDateTime startedAt,
        int duration,
        int cycle
) {}
