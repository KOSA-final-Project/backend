package site.hesil.latteve_spring.domains.project.repository.projectLike;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.hesil.latteve_spring.domains.project.domain.projectLike.ProjectLike;
import site.hesil.latteve_spring.domains.project.domain.projectLike.ProjectLikeId;

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
 * 2024-09-08        Yeong-Huns    좋아요, 좋아요 취소
 */
@Repository
public interface ProjectLikeRepository extends JpaRepository<ProjectLike, ProjectLikeId> {
    Long countProjectLikeByProject_ProjectId(Long project);

    @Modifying
    @Query(value = "INSERT INTO project_like (project_id, member_id) VALUES (:projectId, :memberId)", nativeQuery = true)
    void registerProjectLike(@Param("projectId") Long projectId, @Param("memberId") Long memberId);

    @Modifying
    @Query(value = "DELETE FROM project_like WHERE project_id = :projectId AND member_id = :memberId", nativeQuery = true)
    void deleteProjectLike(@Param("projectId") Long projectId, @Param("memberId") Long memberId);

}
