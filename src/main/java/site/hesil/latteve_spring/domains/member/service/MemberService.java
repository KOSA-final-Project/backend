package site.hesil.latteve_spring.domains.member.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.hesil.latteve_spring.domains.job.domain.Job;
import site.hesil.latteve_spring.domains.job.repository.JobRepository;
import site.hesil.latteve_spring.domains.member.domain.Member;
import site.hesil.latteve_spring.domains.member.domain.memberJob.MemberJob;
import site.hesil.latteve_spring.domains.member.dto.request.RequestMember;
import site.hesil.latteve_spring.domains.member.repository.MemberRepository;
import site.hesil.latteve_spring.domains.member.repository.memberJob.MemberJobRepository;
import site.hesil.latteve_spring.domains.memberStack.domain.MemberStack;
import site.hesil.latteve_spring.domains.memberStack.repository.MemberStackRepository;
import site.hesil.latteve_spring.domains.techStack.domain.TechStack;
import site.hesil.latteve_spring.domains.techStack.repository.TechStackRepository;

/**
 * packageName    : site.hesil.latteve_spring.domains.member.service
 * fileName       : MemberService
 * author         : yunbin
 * date           : 2024-08-28
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-28           yunbin           최초 생성
 */
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberStackRepository memberStackRepository;
    private final MemberJobRepository memberJobRepository;
    private final JobRepository jobRepository;
    private final TechStackRepository techStackRepository;

    @Transactional
    public void registerAdditionalMemberInfo(Long memberId, RequestMember requestMember) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + memberId));

        member.updateProfile(requestMember.nickname(), requestMember.career(), requestMember.github());
        memberRepository.save(member);

        int i = 0;
        for (Long techStackId : requestMember.techStackIds()) {
            TechStack techStack = techStackRepository.findById(techStackId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid job ID: " + techStackId));

            String customStack = null;

            if (techStackId == 1) {
                customStack = requestMember.customStacks().get(i);
                i++;
            }
            MemberStack memberStack = MemberStack.builder()
                    .member(member)
                    .techStack(techStack)
                    .customStack(customStack)
                    .build();
            memberStackRepository.save(memberStack);
        }

        for (Long jobId : requestMember.jobIds()) {
            Job job = jobRepository.findById(jobId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid job ID: " + jobId));

            MemberJob memberJob = MemberJob.builder()
                    .member(member)
                    .job(job)
                    .build();
            memberJobRepository.save(memberJob);
        }
    }

    public boolean checkNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }
}
