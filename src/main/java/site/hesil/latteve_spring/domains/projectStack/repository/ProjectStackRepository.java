package site.hesil.latteve_spring.domains.projectStack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hesil.latteve_spring.domains.projectStack.domain.ProjectStack;

import java.util.List;

/**
 * packageName    : site.hesil.latteve_spring.domains.projectStack.repository
 * fileName       : ProjectStackRepository
 * author         : Heeseon
 * date           : 2024-08-28
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-28        Heeseon       최초 생성
 */
public interface ProjectStackRepository extends JpaRepository<ProjectStack, Long> {
    // 프로젝트 ID로 ProjectStack을 조회
    List<ProjectStack> findAllByProject_ProjectId(Long projectId);


}
