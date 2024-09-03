package site.hesil.latteve_spring.domains.project.repository.projectLike;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.hesil.latteve_spring.domains.project.domain.projectLike.ProjectLike;

/**
 * packageName    : site.hesil.latteve_spring.domains.project.repository.projectlike
 * fileName       : ProjectLikeRepository
 * author         : Heeseon
 * date           : 2024-09-01
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-01        Heeseon       최초 생성
 */
@Repository
public interface ProjectLikeRepository extends JpaRepository<ProjectLike, Long> {
    Long countProjectLikeByProject_ProjectId(Long project);
}
