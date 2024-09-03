package site.hesil.latteve_spring.domains.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hesil.latteve_spring.domains.member.domain.Member;

import java.util.Optional;

/**
 * packageName    : site.hesil.latteve_spring.domains.member.repository
 * fileName       : MemberRepository
 * author         : yunbin
 * date           : 2024-08-23
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-23           yunbin           최초 생성
 * 2024-08-29           yunbin           findByProviderAndProviderId() 추가
 */
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByProviderAndProviderId(String provider, String providerId);
    boolean existsByNickname(String nickname);
}
