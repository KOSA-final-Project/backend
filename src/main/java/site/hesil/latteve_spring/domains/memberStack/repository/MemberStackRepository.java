package site.hesil.latteve_spring.domains.memberStack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.hesil.latteve_spring.domains.memberStack.domain.MemberStack;

import java.util.List;

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
 */
public interface MemberStackRepository extends JpaRepository<MemberStack, Long> {

    List<MemberStack> findAllByMember_MemberId(Long memberId);

    @Query("SELECT CASE " +
            "WHEN ms.techStack.techStackId = 1 THEN ms.customStack " +
            "ELSE ts.name END " +
            "FROM MemberStack ms " +
            "JOIN ms.techStack ts " +
            "WHERE ms.member.memberId = :memberId") // 1이면 커스텀네임사용, 아니면 techStack Name 사용
    List<String> findTechStackNamesByMemberId(@Param("memberId") Long memberId);
}
