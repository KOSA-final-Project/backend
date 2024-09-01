package site.hesil.latteve_spring.domains.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hesil.latteve_spring.domains.member.domain.Member;

/**
 *packageName    : site.hesil.latteve_spring.domains.member.repository
 * fileName       : MemberRepository
 * author         : Heeseon
 * date           : 2024-08-28
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-28        Heeseon       최초 생성
 */public interface MemberRepository extends JpaRepository<Member, Long> {
}
