package site.hesil.latteve_spring.domains.retrospective.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hesil.latteve_spring.domains.retrospective.domain.Retrospective;

/**
 * packageName    : site.hesil.latteve_spring.domains.retrospective.repository
 * fileName       : RetrospectiveRepository
 * author         : JooYoon
 * date           : 2024-09-06
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-06        JooYoon       최초 생성
 */
public interface RetrospectiveRepository extends JpaRepository<Retrospective, Long> {
}
