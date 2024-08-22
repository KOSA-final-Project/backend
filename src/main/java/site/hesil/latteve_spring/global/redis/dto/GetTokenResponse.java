package site.hesil.latteve_spring.global.redis.dto;

import lombok.Builder;

/**
 * packageName    : site.hesil.latteve_spring.global.redis.dto
 * fileName       : GetTokenResponse
 * author         : Yeong-Huns
 * date           : 2024-08-22
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-22        Yeong-Huns       최초 생성
 */
@Builder
public record GetTokenResponse (long userId, String token){
    public static GetTokenResponse of(long userId, String token){
        return new GetTokenResponse(userId, token);
    }
}