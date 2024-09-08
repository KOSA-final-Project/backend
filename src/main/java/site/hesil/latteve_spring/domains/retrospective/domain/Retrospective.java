package site.hesil.latteve_spring.domains.retrospective.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.hesil.latteve_spring.domains.job.domain.Job;
import site.hesil.latteve_spring.domains.member.domain.Member;
import site.hesil.latteve_spring.domains.project.domain.Project;
import site.hesil.latteve_spring.global.audit.entity.BaseTimeEntity;

/**
 * packageName    : site.hesil.latteve_spring.domains.retrospective.domain
 * fileName       : Retrospective
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
public class Retrospective extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long retId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;

    private String title;
    private String content;
    private int week;

    private Retrospective(Project project, Member member, Job job, String title, String content, int week) {
        this.project = project;
        this.member = member;
        this.job = job;
        this.title = title;
        this.content = content;
        this.week = week;
    }

    public static Retrospective of(Project project, Member member, Job job, String title, String content, int week) {
        return new Retrospective(project, member, job, title, content, week);
    }
}
