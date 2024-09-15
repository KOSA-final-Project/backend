package site.hesil.latteve_spring.domains.project.domain.projectMember;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.hesil.latteve_spring.domains.job.domain.Job;
import site.hesil.latteve_spring.domains.member.domain.Member;
import site.hesil.latteve_spring.domains.project.domain.Project;
import site.hesil.latteve_spring.domains.project.listener.ProjectLikeListener;
import site.hesil.latteve_spring.domains.project.listener.ProjectMemberListener;

/**
 * packageName    : site.hesil.latteve_spring.domains.projectMember.domain
 * fileName       : ProjectMember
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
@EntityListeners(ProjectMemberListener.class)
public class ProjectMember {

    @EmbeddedId
    private ProjectMemberId projectMemberId;

    @MapsId("projectId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @MapsId("memberId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @MapsId("jobId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;

    @Column(columnDefinition = "TINYINT(1)")
    private boolean isLeader;

    @Column(columnDefinition = "INT DEFAULT 2")
    private int acceptStatus;

    private ProjectMember(Project project, Member member, Job job) {
        this.projectMemberId = new ProjectMemberId(project.getProjectId(), member.getMemberId(), job.getJobId());
        this.project = project;
        this.member = member;
        this.job = job;
        this.isLeader = false;
        this.acceptStatus = 2;
    }

    public static ProjectMember of(Project project, Member member, Job job) {
        return new ProjectMember(project, member, job);
    }

    public void updateAcceptStatus(int status) {
        this.acceptStatus = status;
    }
}
