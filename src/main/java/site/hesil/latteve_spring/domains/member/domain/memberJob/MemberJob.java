package site.hesil.latteve_spring.domains.member.domain.memberJob;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.hesil.latteve_spring.domains.job.domain.Job;
import site.hesil.latteve_spring.domains.member.domain.Member;

/**
 * packageName    : site.hesil.latteve_spring.domains.member.domain
 * fileName       : MemberJob
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
public class MemberJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberJobId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;

    @Builder
    public MemberJob(Member member, Job job) {
        this.member = member;
        this.job = job;
    }
}
