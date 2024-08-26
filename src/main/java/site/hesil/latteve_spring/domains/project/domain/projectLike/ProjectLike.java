package site.hesil.latteve_spring.domains.project.domain.projectLike;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.hesil.latteve_spring.domains.member.domain.Member;
import site.hesil.latteve_spring.domains.project.domain.Project;

/**
 * packageName    : site.hesil.latteve_spring.domains.project.domain.projectLike
 * fileName       : ProjectLike
 * author         : JooYoon
 * date           : 2024-08-26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-26        JooYoon       최초 생성
 */

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectLike {

    @EmbeddedId
    private ProjectLikeId projectLikeId;

    @MapsId("projectId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @MapsId("memberId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
