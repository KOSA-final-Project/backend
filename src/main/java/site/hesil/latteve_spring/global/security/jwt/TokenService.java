package site.hesil.latteve_spring.global.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hesil.latteve_spring.domains.member.domain.Member;
import site.hesil.latteve_spring.domains.member.repository.MemberRepository;
import site.hesil.latteve_spring.global.error.exception.TokenException;

import java.util.Optional;

import static site.hesil.latteve_spring.global.error.errorcode.ErrorCode.TOKEN_EXPIRED;

/**
 * packageName    : site.hesil.latteve_spring.global.security
 * fileName       : TokenService
 * author         : yunbin
 * date           : 2024-08-29
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-29           yunbin           최초 생성
 * 2024-09-03           yunbin           redis에 memeberId 추가 저장
 */
@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenRepository tokenRepository;
    private final MemberRepository memberRepository;

    public void deleteRefreshToken(String memberKey) {
        tokenRepository.deleteById(memberKey);
    }

    @Transactional
    public void saveOrUpdate(String email, String refreshToken, String accessToken) {
        Optional<Member> _member = memberRepository.findByEmail(email);

        Long memberId;

        if (_member.isPresent()) {
            Member member = _member.get();
            memberId = member.getMemberId();
        } else {
            memberId = 0L;
        }

        Token token = tokenRepository.findByAccessToken(accessToken)
                .map(o -> o.updateRefreshToken(refreshToken))
                .orElseGet(() -> new Token(email, memberId, refreshToken, accessToken));

        tokenRepository.save(token);
    }

    public Token findByAccessTokenOrThrow(String accessToken) {
        return tokenRepository.findByAccessToken(accessToken)
                .orElseThrow(() -> new TokenException(TOKEN_EXPIRED));
    }

    @Transactional
    public void updateToken(String accessToken, Token token) {
        token.updateAccessToken(accessToken);
        tokenRepository.save(token);
    }
}