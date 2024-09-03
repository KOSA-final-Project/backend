package site.hesil.latteve_spring.domains.member.repository.memberJob;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.hesil.latteve_spring.domains.member.domain.memberJob.MemberJob;

import java.util.List;

/**
 * packageName    : site.hesil.latteve_spring.domains.member.repository.memberjob
 * fileName       : MemberJobRepository
 * author         : Heeseon
 * date           : 2024-08-31
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-31        Heeseon       최초 생성
 */
@Repository
public interface MemberJobRepository extends JpaRepository<MemberJob, Long> {
    List<MemberJob> findAllByMember_MemberId(Long memberId);
}
