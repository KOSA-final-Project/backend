package site.hesil.latteve_spring.global.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hesil.latteve_spring.domains.member.domain.Member;
import site.hesil.latteve_spring.domains.member.repository.MemberRepository;

import java.util.Map;
import java.util.Optional;

/**
 * packageName    : site.hesil.latteve_spring.global.security
 * fileName       : CustomOAuth2UserService
 * author         : yunbin
 * date           : 2024-08-23
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-23           yunbin           최초 생성
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 1. 유저 정보(attributes) 가져오기
        Map<String, Object> oAuth2UserAttributes = super.loadUser(userRequest).getAttributes();

        // 2. resistrationId 가져오기 (third-party id) == provider
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // 3. userNameAttributeName 가져오기
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        // 4. 유저 정보 dto 생성
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfo.of(registrationId, oAuth2UserAttributes);

        // 5. 같은 이메일인데 다른 소셜 로그인으로 가입 시도했을 경우 -> 가입 x
        Optional<Member> _existUser = memberRepository.findByEmail(oAuth2UserInfo.email());
        if (_existUser.isPresent()) {
            Member existUser = _existUser.get();
            // 기존 사용자가 존재하고, 다른 소셜 로그인 제공자로 가입된 경우
            if (!existUser.getProvider().equals(registrationId)) {
                // 예외를 던져서 사용자에게 메시지 반환
                throw new OAuth2AuthenticationException(
                        new OAuth2Error("duplicate_email"),
                        String.format("%s은(는) %s로 가입된 이메일입니다.",
                                oAuth2UserInfo.email(),
                                existUser.getProvider())
                );
            }
        }

        // 5. 회원가입 및 로그인
        Member member = getOrSave(oAuth2UserInfo);

        PrincipalDetails principalDetails = new PrincipalDetails(member, oAuth2UserAttributes, userNameAttributeName, registrationId);
        log.info(principalDetails.toString());
        // 6. OAuth2User로 반환
        return principalDetails;
    }

    private Member getOrSave(OAuth2UserInfo oAuth2UserInfo) {
        Member member = memberRepository.findByEmail(oAuth2UserInfo.email())
                .orElseGet(oAuth2UserInfo::toEntity);
        return memberRepository.save(member);
    }
}