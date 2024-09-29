package site.hesil.latteve_spring.domains.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
//    private final MemberRepository memberRepository;
//    private final MemberStackRepository memberStackRepository;
//    private final MemberJobRepository memberJobRepository;
//    private final JobRepository jobRepository;
//    private final TechStackRepository techStackRepository;
//    private final S3Service s3Service;
//    private final MQSender mqSender;
//
//    @Transactional
//    public void registerAdditionalMemberInfo(Long memberId, RequestMember requestMember) {
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + memberId));
//
//        member.updateProfile(requestMember.nickname(), requestMember.career(), requestMember.github());
//        memberRepository.save(member);
//        mqSender.sendMessage(MQExchange.DIRECT_MEMBER.getExchange(), MQRouting.MEMBER_CREATE.getRouting(), member);
//        int i = 0;
//        for (Long techStackId : requestMember.techStackIds()) {
//            TechStack techStack = techStackRepository.findById(techStackId)
//                    .orElseThrow(() -> new IllegalArgumentException("Invalid job ID: " + techStackId));
//
//            String customStack = null;
//
//            if (techStackId == 1) {
//                customStack = requestMember.customStacks().get(i);
//                i++;
//            }
//            MemberStack memberStack = MemberStack.builder()
//                    .member(member)
//                    .techStack(techStack)
//                    .customStack(customStack)
//                    .build();
//            memberStackRepository.save(memberStack);
//        }
//
//        for (Long jobId : requestMember.jobIds()) {
//            Job job = jobRepository.findById(jobId)
//                    .orElseThrow(() -> new IllegalArgumentException("Invalid job ID: " + jobId));
//
//            MemberJob memberJob = MemberJob.builder()
//                    .member(member)
//                    .job(job)
//                    .build();
//            memberJobRepository.save(memberJob);
//        }
//    }
//
//    public boolean checkNickname(String nickname) {
//        return memberRepository.existsByNickname(nickname);
//    }
//
//    public MemberResponse getMemberInfo(Long memberId){
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + memberId));
//        // 멤버의 기술 스택 정보
//        List<MemberStack> memberStacks = memberStackRepository.findAllByMember_MemberId(memberId);
//
//        // techStack List로 저장
//        List<MemberResponse.TechStack> techStackList = new ArrayList<>();
//        for(MemberStack memberStack : memberStacks){
//            Long techStackId = memberStack.getTechStack().getTechStackId();
//            if(techStackId == 1){
//                techStackList.add(new MemberResponse.TechStack(memberStack.getCustomStack(), null));
//            }else {
//                Optional<TechStack> techStackOpt = techStackRepository.findById(memberStack.getTechStack().getTechStackId());
//                if (techStackOpt.isPresent()) {
//                    TechStack techStack = techStackOpt.get();
//                    String name = techStack.getName();
//                    String imgUrl = techStack.getImgUrl();
//
//                    // TechStack 객체를 리스트에 추가
//                    techStackList.add(new MemberResponse.TechStack(name, imgUrl));
//                }
//            }
//        }
//        // 멤버의 직무 정보 가져옴
//        List<MemberJob> memberJobs = memberJobRepository.findAllByMember_MemberId(member.getMemberId());
//        List<String> jobList = new ArrayList<>();
//        // 멤버의 직무 이름 list로 저장
//        for(MemberJob memberJob : memberJobs) {
//            Optional<Job> jobOpt = jobRepository.findById(memberJob.getJob().getJobId());
//            jobOpt.ifPresent(job -> jobList.add(job.getName()));
//        }
//
//        MemberResponse memberResponse =  MemberResponse.builder()
//                .memberId(member.getMemberId())
//                .imgUrl(member.getImgUrl())
//                .email(member.getEmail())
//                .nickname(member.getNickname())
//                .github(member.getGithub())
//                .career(member.getCareer())
//                .jobs(jobList)
//                .pr(member.getPr())
//                .memberTechStack(techStackList ).build();
//
//
//        return memberResponse;
//    }
//    @Transactional
//    public void updateMemberProfile(Long  memberId, UpdateMemberReq updateRequest) {
//        // 멤버 정보 조회 및 예외 처리
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new CustomBaseException("해당 멤버가 존재하지 않습니다. " , USER_NOT_FOUND));
//
//        // 멤버 정보 변경
//        member.updateMemberInfo(updateRequest.imgUrl(), updateRequest.nickname(), updateRequest.career(), updateRequest.github(), updateRequest.pr());
//        memberRepository.save(member);
//
//
//        // 기술 스택 저장
//        int i = 0;
//        for (Long techStackId : updateRequest.techStackIds()) {
//            TechStack techStack = techStackRepository.findById(techStackId)
//                    .orElseThrow(() -> new IllegalArgumentException("Invalid tech stack ID: " + techStackId));
//
//            String customStack = null;
//
//            // Custom Stack이 있는 경우 처리
//            if (techStackId == 1 && i < updateRequest.customStacks().size()) {
//                customStack = updateRequest.customStacks().get(i);
//                i++;
//            }
//
//            // MemberStack 객체 생성 및 저장
//            MemberStack memberStack = MemberStack.builder()
//                    .member(member)
//                    .techStack(techStack)
//                    .customStack(customStack)
//                    .build();
//            memberStackRepository.save(memberStack);
//        }
//
//        // 직무 정보 저장
//        for (Long jobId : updateRequest.jobIds()) {
//            Job job = jobRepository.findById(jobId)
//                    .orElseThrow(() ->  new CustomBaseException("해당 직업이 존재하지 않습니다." , NOT_FOUND));
//
//            // MemberJob 객체 생성 및 저장 (builder 패턴 사용)
//            MemberJob memberJob = MemberJob.builder()
//                    .member(member)
//                    .job(job)
//                    .build();
//            memberJobRepository.save(memberJob);
//        }
//    }
}
