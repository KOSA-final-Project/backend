package site.hesil.latteve_spring.global.security;

import lombok.Builder;
import site.hesil.latteve_spring.domains.member.domain.Member;
import site.hesil.latteve_spring.global.error.exception.AuthException;

import java.util.Map;

import static site.hesil.latteve_spring.global.error.errorcode.ErrorCode.ILLEGAL_REGISTRATION_ID;

/**
 * packageName    : site.hesil.latteve_spring.global.security
 * fileName       : OAuth2UserInfo
 * author         : yunbin
 * date           : 2024-08-23
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-23           yunbin           최초 생성
 */
@Builder
public record OAuth2UserInfo(
        String email,
        String profile
) {

    public static OAuth2UserInfo of(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId) { // registration id별로 userInfo 생성
            case "google" -> ofGoogle(attributes);
            case "kakao" -> ofKakao(attributes);
            default -> throw new AuthException(ILLEGAL_REGISTRATION_ID);
        };
    }

    private static OAuth2UserInfo ofGoogle(Map<String, Object> attributes) {
        return OAuth2UserInfo.builder()
                .email((String) attributes.get("email"))
                .profile((String) attributes.get("picture"))
                .build();
    }

    private static OAuth2UserInfo ofKakao(Map<String, Object> attributes) {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        return OAuth2UserInfo.builder()
                .email((String) account.get("email"))
                .profile((String) profile.get("profile_image_url"))
                .build();
    }

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .imgUrl(profile)
                .build();
    }
}