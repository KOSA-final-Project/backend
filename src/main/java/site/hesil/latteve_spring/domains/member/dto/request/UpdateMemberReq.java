package site.hesil.latteve_spring.domains.member.dto.request;

import site.hesil.latteve_spring.domains.member.dto.response.MemberResponse;

import java.util.List;

/**
 * packageName    : site.hesil.latteve_spring.domains.member.dto.request
 * fileName       : UpdateMemberReq
 * author         : Heeseon
 * date           : 2024-09-10
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-10        Heeseon       최초 생성
 */
public record UpdateMemberReq(
        Long memberId,
        String imgUrl,
        String nickname,
        String github,
        List<Long> jobIds,
        String career,
        List<Long> techStackIds,
        List<String> customStacks,
        String pr
) {

}
