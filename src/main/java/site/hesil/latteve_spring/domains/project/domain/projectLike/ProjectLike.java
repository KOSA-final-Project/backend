package site.hesil.latteve_spring.domains.project.domain.projectLike;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.hesil.latteve_spring.domains.member.domain.Member;
import site.hesil.latteve_spring.domains.project.domain.Project;
import site.hesil.latteve_spring.domains.project.listener.ProjectLikeListener;

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
 * 2024-09-15        Heeseon       Builder 추가
 */

@Getter
@Entity
@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(ProjectLikeListener.class)
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

    @Builder
    public ProjectLike(Project project,  Member member) {
        this.project = project;
        this.member = member;
        this.projectLikeId = new ProjectLikeId(project.getProjectId(), member.getMemberId());
    }
}
