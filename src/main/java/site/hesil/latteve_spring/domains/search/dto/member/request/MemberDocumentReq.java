package site.hesil.latteve_spring.domains.search.dto.member.request;

import lombok.Builder;
import site.hesil.latteve_spring.domains.techStack.domain.TechStack;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * packageName    : site.hesil.latteve_spring.domains.search.dto.member.request
 * fileName       : MemberDocumentReq
 * author         : Heeseon
 * date           : 2024-08-29
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-29        Heeseon       최초 생성
 */
@Builder
public record MemberDocumentReq (
        Long memberId,
        String memberNickname,
        String memberImg,
        String memberGithub,
        TechStack techStacks,
        String career,
        String createdAt
){}