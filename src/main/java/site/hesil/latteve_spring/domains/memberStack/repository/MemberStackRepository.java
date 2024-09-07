package site.hesil.latteve_spring.domains.memberStack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.hesil.latteve_spring.domains.memberStack.domain.MemberStack;
import site.hesil.latteve_spring.domains.memberStack.dto.response.MemberStackResponse;

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
 * 2024-09-07        Yeong-Huns    DTO 반환
 */
public interface MemberStackRepository extends JpaRepository<MemberStack, Long> {

    List<MemberStack> findAllByMember_MemberId(Long memberId);

    @Query("""
             SELECT new site.hesil.latteve_spring.domains.memberStack.dto.response.MemberStackResponse(
                 CASE
                     WHEN ms.techStack.techStackId = 1 THEN ms.customStack
                     ELSE ts.name
                 END,
                 CASE
                     WHEN ms.techStack.techStackId = 1 THEN null
                     ELSE ts.imgUrl
                 END
             )
             FROM MemberStack ms
             JOIN ms.techStack ts
             WHERE ms.member.memberId = :memberId
            """)
    List<MemberStackResponse> findTechStackNamesByMemberId(@Param("memberId") Long memberId);
}
