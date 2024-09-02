package site.hesil.latteve_spring.domains.projectStack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.hesil.latteve_spring.domains.project.dto.request.projectSave.TechStack;
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
 * 2024-09-01        Yeong-Huns    프로젝트 생성 커스텀 로직 추가
 */
public interface ProjectStackRepository extends JpaRepository<ProjectStack, Long> {
    // 프로젝트 ID로 ProjectStack을 조회
    List<ProjectStack> findAllByProject_ProjectId(Long projectId);

    @Modifying
    @Query(value = "INSERT INTO project_stack (project_id, tech_stack_id, custom_stack) VALUES (:projectId, :techStackId, CASE WHEN :techStackId = 1 THEN :customStack ELSE NULL END)", nativeQuery = true)
    void saveProjectStack(@Param("projectId") Long projectId, @Param("techStackId") Long techStackId, @Param("customStack") String customStack);

    default void saveAllProjectStacks(List<TechStack> techStacks, Long projectId) {
        for (TechStack stack : techStacks) {
            saveProjectStack(projectId, stack.techStackId(), stack.techStackId() == 1 ? stack.name() : null);
        }
    }
}
