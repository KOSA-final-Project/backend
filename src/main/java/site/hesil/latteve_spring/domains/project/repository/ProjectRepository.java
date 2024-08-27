package site.hesil.latteve_spring.domains.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hesil.latteve_spring.domains.project.domain.Project;
import site.hesil.latteve_spring.domains.project.repository.custom.ProjectRepositoryCustom;

/**
 * packageName    : site.hesil.latteve_spring.domains.project.repository
 * fileName       : ProjectRepository
 * author         : JooYoon
 * date           : 2024-08-27
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-27        JooYoon       최초 생성
 */

public interface ProjectRepository extends JpaRepository<Project, Long>, ProjectRepositoryCustom {
}
