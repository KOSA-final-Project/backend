package site.hesil.latteve_spring.domains.alarm.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.hesil.latteve_spring.domains.job.domain.Job;
import site.hesil.latteve_spring.domains.member.domain.Member;
import site.hesil.latteve_spring.domains.project.domain.Project;

/**
 * packageName    : site.hesil.latteve_spring.domains.alarm.domain
 * fileName       : Alarm
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
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long alarmId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;

    @Column(columnDefinition = "TINYINT(1)")
    private Integer type;

    @Column(columnDefinition = "TINYINT(1)")
    private boolean isRead;

    @Builder
    private Alarm(Project project, Member member, Job job, Integer type) {
        this.project = project;
        this.member = member;
        this.job = job;
        this.type = type;
        this.isRead = false;
    }

    public void updateAcceptStatus(int acceptStatus){
        type = acceptStatus;
    }

    public static Alarm of(Project project, Member member, Job job, Integer type) {
        return new Alarm(project, member, job, type);
    }
}
