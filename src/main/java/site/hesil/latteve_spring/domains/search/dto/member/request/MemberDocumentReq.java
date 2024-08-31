package site.hesil.latteve_spring.domains.search.dto.member.request;

import lombok.Builder;
import site.hesil.latteve_spring.domains.job.domain.Job;

import java.util.List;


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
        List<TechStack> techStack,
        List<String> memberJob,
        String createdAt
){
    public record TechStack(
        String name,
        String imgUrl
    ){}
}