package site.hesil.latteve_spring.domains.project.repository.project;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.hesil.latteve_spring.domains.project.domain.Project;
import site.hesil.latteve_spring.domains.project.repository.project.custom.ProjectRepositoryCustom;
import site.hesil.latteve_spring.domains.projectStack.domain.ProjectStack;

import java.util.List;

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
 * 2024
 */

public interface ProjectRepository extends JpaRepository<Project, Long>, ProjectRepositoryCustom {
    @Query("SELECT p FROM ProjectLike pl JOIN pl.project p WHERE pl.member.memberId = :memberId")
    List<Project> findLikedProjectsByMemberId(@Param("memberId") Long memberId);
}