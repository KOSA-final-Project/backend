package site.hesil.latteve_spring.domains.memberStack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hesil.latteve_spring.domains.memberStack.domain.MemberStack;

import java.util.List;
import java.util.Optional;

/**
 * packageName    : site.hesil.latteve_spring.domains.memberStack.repository
 * fileName       : MemberStackRepository
 * author         : Heeseon
 * date           : 2024-08-29
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-29        Heeseon       최초 생성
 */
public interface MemberStackRepository extends JpaRepository<MemberStack, Long> {

    List<MemberStack> findAllByMember_MemberId(Long memberId);
}
