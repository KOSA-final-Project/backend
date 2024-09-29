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
 * 2024-09-08        Yeong-Huns    좋아요, 좋아요 취소
 * 2024-09-07        Heeseon       사용자 아이디와 프로젝트 아이디로 좋아요 조회
 */
@Repository
public interface ProjectLikeRepository extends JpaRepository<ProjectLike, Long> {
//    Long countProjectLikeByProject_ProjectId(Long project);
//
//    void registerProjectLike(@Param("projectId") Long projectId, @Param("memberId") Long memberId);
//
//    void deleteProjectLike(@Param("projectId") Long projectId, @Param("memberId") Long memberId);
//
//    boolean existsByProject_ProjectIdAndMember_MemberId(Long projectId, Long memberId);
//
//    List<Long> findLikedProjectIdsByMemberIdAndProjectIds(@Param("memberId") Long memberId, @Param("projectIds") List<Long> projectIds);
//
//    Long findMaxLikes();
}
