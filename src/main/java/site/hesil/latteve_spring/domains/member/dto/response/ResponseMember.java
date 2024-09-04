package site.hesil.latteve_spring.domains.member.dto.response;

import lombok.Builder;

import java.util.List;

/**
 * packageName    : site.hesil.latteve_spring.domains.member.dto.response
 * fileName       : ResponseMember
 * author         : Heeseon
 * date           : 2024-09-04
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-04        Heeseon       최초 생성
 */
@Builder
public record ResponseMember (
    Long memberId,
    String imgUrl,
    String email,
    String nickname,
    String github,
    String career,
    List<TechStack> memberTechStack,
    String pr // 자기소개
){
    public record TechStack(
            String name,
            String imgUrl
    ){}
}
