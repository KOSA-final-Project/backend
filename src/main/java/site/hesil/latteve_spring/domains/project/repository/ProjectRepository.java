package site.hesil.latteve_spring.domains.project.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import site.hesil.latteve_spring.domains.project.domain.Project;

/**
 * packageName    : site.hesil.latteve_spring.domains.project.repository
 * fileName       : ProjectRepository
 * author         : Heeseon
 * date           : 2024-08-28
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-28        Heeseon       최초 생성
 */
public interface ProjectRepository extends JpaRepository<Project, Long> {

}
