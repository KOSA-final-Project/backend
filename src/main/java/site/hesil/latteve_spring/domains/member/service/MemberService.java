package site.hesil.latteve_spring.domains.member.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.hesil.latteve_spring.domains.job.domain.Job;
import site.hesil.latteve_spring.domains.job.repository.JobRepository;
import site.hesil.latteve_spring.domains.member.domain.Member;
import site.hesil.latteve_spring.domains.member.domain.memberJob.MemberJob;
import site.hesil.latteve_spring.domains.member.dto.request.RequestMember;
import site.hesil.latteve_spring.domains.member.dto.response.ResponseMember;
import site.hesil.latteve_spring.domains.member.repository.MemberRepository;
import site.hesil.latteve_spring.domains.member.repository.memberJob.MemberJobRepository;
import site.hesil.latteve_spring.domains.memberStack.domain.MemberStack;
import site.hesil.latteve_spring.domains.memberStack.repository.MemberStackRepository;
import site.hesil.latteve_spring.domains.search.dto.member.request.MemberDocumentReq;
import site.hesil.latteve_spring.domains.techStack.domain.TechStack;
import site.hesil.latteve_spring.domains.techStack.repository.TechStackRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
 * 2024-09-04           Heeseon          사용자 정보 조회
 */

@Slf4j
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

    public ResponseMember getMemberInfo(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + memberId));
        // 멤버의 기술 스택 정보
        List<MemberStack> memberStacks = memberStackRepository.findAllByMember_MemberId(memberId);

        // techStack List로 저장
        List<ResponseMember.TechStack> techStackList = new ArrayList<>();
        for(MemberStack memberStack : memberStacks){
            Long techStackId = memberStack.getTechStack().getTechStackId();
            if(techStackId == 1){
                techStackList.add(new ResponseMember.TechStack(memberStack.getCustomStack(), null));
            }else {
                Optional<TechStack> techStackOpt = techStackRepository.findById(memberStack.getTechStack().getTechStackId());
                if (techStackOpt.isPresent()) {
                    TechStack techStack = techStackOpt.get();
                    String name = techStack.getName();
                    String imgUrl = techStack.getImgUrl();

                    // TechStack 객체를 리스트에 추가
                    techStackList.add(new ResponseMember.TechStack(name, imgUrl));
                }
            }
        }
        // 멤버의 직무 정보 가져옴
        List<MemberJob> memberJobs = memberJobRepository.findAllByMember_MemberId(member.getMemberId());
        List<String> jobList = new ArrayList<>();
        // 멤버의 직무 이름 list로 저장
        for(MemberJob memberJob : memberJobs) {
            Optional<Job> jobOpt = jobRepository.findById(memberJob.getJob().getJobId());
            jobOpt.ifPresent(job -> jobList.add(job.getName()));
        }

        ResponseMember responseMember =  ResponseMember.builder()
                .memberId(member.getMemberId())
                .imgUrl(member.getImgUrl())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .github(member.getGithub())
                .career(member.getCareer())
                .jobs(jobList)
                .pr(member.getPr())
                .memberTechStack(techStackList ).build();


        return responseMember;
    }

}
