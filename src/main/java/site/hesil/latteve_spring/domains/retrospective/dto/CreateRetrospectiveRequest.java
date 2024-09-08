package site.hesil.latteve_spring.domains.retrospective.dto;

/**
 * packageName    : site.hesil.latteve_spring.domains.retrospective.dto
 * fileName       : CreateRetrospectiveRequest
 * author         : JooYoon
 * date           : 2024-09-06
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-06        JooYoon       최초 생성
 */
public record CreateRetrospectiveRequest(int week, String title, String content) {
}