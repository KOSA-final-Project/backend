package site.hesil.latteve_spring.domains.project.repository.projectLike;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;
import site.hesil.latteve_spring.domains.project.domain.projectLike.ProjectLike;
import site.hesil.latteve_spring.domains.project.domain.projectLike.ProjectLikeId;

import java.util.List;

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
 * 2024-09-07        Heeseon       사용자 아이디와 프로젝트 아이디로 좋아요 조회
 */
@Repository
public interface ProjectLikeRepository extends JpaRepository<ProjectLike, ProjectLikeId> {
    Long countProjectLikeByProject_ProjectId(Long project);


    @Modifying
    @Query(value = "INSERT INTO project_like (project_id, member_id) VALUES (:projectId, :memberId)", nativeQuery = true)
    void registerProjectLike(@Param("projectId") Long projectId, @Param("memberId") Long memberId);

    @Modifying
    @Query(value = "DELETE FROM projectLike p WHERE p.project.projectId = :projectId AND p.member.memberId = :memberId", nativeQuery = true)
    void deleteProjectLike(@Param("projectId") Long projectId, @Param("memberId") Long memberId);


    boolean existsByProject_ProjectIdAndMember_MemberId(Long projectId, Long memberId);

    @Query("SELECT pl.project.projectId FROM ProjectLike pl WHERE pl.member.memberId = :memberId AND pl.project.projectId IN :projectIds")
    List<Long> findLikedProjectIdsByMemberIdAndProjectIds(@Param("memberId") Long memberId, @Param("projectIds") List<Long> projectIds);

    // 프로젝트별 좋아요 수 중 가장 많은 좋아요 수 반환
    @Query("SELECT COUNT(pl) FROM ProjectLike pl GROUP BY pl.project ORDER BY COUNT(pl) DESC")
    Long findMaxLikes();
}
