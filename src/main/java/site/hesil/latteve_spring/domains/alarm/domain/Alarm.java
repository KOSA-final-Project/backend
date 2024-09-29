package site.hesil.latteve_spring.domains.alarm.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.hesil.latteve_spring.domains.member.domain.Member;
import site.hesil.latteve_spring.domains.project.domain.recruitment.Recruitment;

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
    @JoinColumn(name = "recruitment_id")
    private Recruitment recruitment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(columnDefinition = "TINYINT")
    private int type;

    @Column(columnDefinition = "TINYINT")
    private boolean isRead;

//    @Builder
//    private Alarm(Member member, Job job, Integer type) {
//        this.member = member;
//        this.job = job;
//        this.type = type;
//        this.isRead = false;
//    }

    public void updateAcceptStatus(int acceptStatus){
        type = acceptStatus;
        isRead = false;
    }

    public void read() {
        isRead = true;
    }

//    public static Alarm of(Project project, Member member, Job job, Integer type) {
//        return new Alarm(project, member, job, type);
//    }
}
