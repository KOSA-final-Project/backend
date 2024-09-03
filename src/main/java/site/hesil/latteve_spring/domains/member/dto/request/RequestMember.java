package site.hesil.latteve_spring.domains.member.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * packageName    : site.hesil.latteve_spring.domains.member.dto.request
 * fileName       : RequestMember
 * author         : yunbin
 * date           : 2024-08-28
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-28           yunbin           최초 생성
 */
public record RequestMember(
        @NotEmpty(message = "닉네임을 입력해 주세요.") String nickname,
        @NotEmpty(message = "기술 스택을 선택해 주세요.") @Size(max=10, message = "기술 스택은 최대 10개까지 선택 가능합니다.") List<Long> techStackIds,
        List<String> customStacks,
        @NotEmpty(message = "직무을 선택해 주세요.") List<Long> jobIds,
        @NotEmpty(message = "경력을 선택해 주세요.") String career,
        String github) {
}
