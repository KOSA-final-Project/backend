package site.hesil.latteve_spring.domains.memberStack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hesil.latteve_spring.domains.memberStack.domain.MemberStack;

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
 * 2024-08-29        Yeong-Huns    MemberID 로 ProjectStack 조회
 * 2024-09-07        Yeong-Huns    DTO 반환
 */
public interface MemberStackRepository extends JpaRepository<MemberStack, Long> {
//
//    List<MemberStack> findAllByMember_MemberId(Long memberId);
//
//    List<MemberStack> findAllTechStacksByMemberIds(@Param("memberIds") List<Long> memberIds);
}
