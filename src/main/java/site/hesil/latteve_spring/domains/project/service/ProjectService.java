package site.hesil.latteve_spring.domains.project.service;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hesil.latteve_spring.domains.alarm.domain.Alarm;
import site.hesil.latteve_spring.domains.alarm.dto.ProjectApplicationAlarm;
import site.hesil.latteve_spring.domains.alarm.dto.ProjectApprovalResultAlarm;
import site.hesil.latteve_spring.domains.alarm.repository.AlarmRepository;
import site.hesil.latteve_spring.domains.job.domain.Job;
import site.hesil.latteve_spring.domains.job.repository.JobRepository;
import site.hesil.latteve_spring.domains.member.domain.Member;
import site.hesil.latteve_spring.domains.member.repository.MemberRepository;
import site.hesil.latteve_spring.domains.memberStack.domain.MemberStack;
import site.hesil.latteve_spring.domains.memberStack.dto.response.MemberStackResponse;
import site.hesil.latteve_spring.domains.memberStack.repository.MemberStackRepository;
import site.hesil.latteve_spring.domains.project.domain.Project;
import site.hesil.latteve_spring.domains.project.domain.projectMember.ProjectMember;
import site.hesil.latteve_spring.domains.project.dto.project.request.UpdateAcceptStatusRequest;
import site.hesil.latteve_spring.domains.project.dto.project.response.PopularProjectResponse;
import site.hesil.latteve_spring.domains.project.dto.project.response.ProjectCardResponse;
import site.hesil.latteve_spring.domains.project.dto.project.response.ProjectDetailResponse;
import site.hesil.latteve_spring.domains.project.dto.project.response.ProjectMemberResponse;
import site.hesil.latteve_spring.domains.project.dto.project.response.projectDetail.*;
import site.hesil.latteve_spring.domains.project.dto.request.projectSave.ProjectSaveRequest;
import site.hesil.latteve_spring.domains.project.dto.response.ApplicationResponse;
import site.hesil.latteve_spring.domains.project.dto.response.RetrospectiveResponse;
import site.hesil.latteve_spring.domains.project.repository.project.ProjectRepository;
import site.hesil.latteve_spring.domains.project.repository.projectLike.ProjectLikeRepository;
import site.hesil.latteve_spring.domains.project.repository.projectMember.ProjectMemberRepository;
import site.hesil.latteve_spring.domains.project.repository.recruitment.RecruitmentRepository;
import site.hesil.latteve_spring.domains.projectStack.domain.ProjectStack;
import site.hesil.latteve_spring.domains.projectStack.repository.ProjectStackRepository;
import site.hesil.latteve_spring.domains.retrospective.domain.Retrospective;
import site.hesil.latteve_spring.domains.retrospective.dto.CreateRetrospectiveRequest;
import site.hesil.latteve_spring.domains.retrospective.repository.RetrospectiveRepository;
import site.hesil.latteve_spring.domains.techStack.domain.TechStack;
import site.hesil.latteve_spring.domains.techStack.repository.TechStackRepository;
import site.hesil.latteve_spring.global.error.errorcode.ErrorCode;
import site.hesil.latteve_spring.global.error.exception.CustomBaseException;
import site.hesil.latteve_spring.global.error.exception.NotFoundException;
import site.hesil.latteve_spring.global.rabbitMQ.enumerate.MQExchange;
import site.hesil.latteve_spring.global.rabbitMQ.enumerate.MQRouting;
import site.hesil.latteve_spring.global.rabbitMQ.publisher.MQSender;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * packageName    : site.hesil.latteve_spring.domains.project.service
 * fileName       : ProjectService
 * author         : JooYoon
 * date           : 2024-08-26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-26        JooYoon       최초 생성
 * 2024-09-01        Yeong-Huns    프로젝트 생성
 * 2024-09-04        Heeseon       프로젝트 조회, 프로젝트 카드 내용 조회
 * 2024-09-07        Yeong-Huns    프로젝트 지원자 승인 / 거절
 * 2024-09-08        Heeseon       좋아요 여부 확인 추가
 * 2024-09-08        Yeong-Huns    좋아요, 좋아요 취소
 * 2024-09-11        Yeong-Huns    getApplicationsByProjectId 쿼리 성능개선
 * 2024-09-11        Yeong-Huns    applyProject 이미 지원중인 인원인지 검증로직 추가
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final MemberRepository memberRepository;
    private final JobRepository jobRepository;
    private final AlarmRepository alarmRepository;
    private final ProjectStackRepository projectStackRepository;
    private final RecruitmentRepository recruitmentRepository;
    private final MemberStackRepository memberStackRepository;
    private final TechStackRepository techStackRepository;
    private final ProjectLikeRepository projectLikeRepository;
    private final RetrospectiveRepository retrospectiveRepository;
    private final MQSender mqSender;

    // 프로젝트 상세 페이지 정보
    @Transactional(readOnly = true)
    public ProjectDetailResponse getProjectDetail(Long projectId) {
        long startTime = System.currentTimeMillis();
        ProjectDetailResponse result = projectRepository.getProjectDetail_deprecated(projectId)
                .orElseThrow(() -> new CustomBaseException(ErrorCode.NOT_FOUND));
        long endTime = System.currentTimeMillis();
        log.info("getProjectDetail 쿼리 시간 : {} ms", (endTime - startTime));
        return result;
    }

    @Transactional
    public long saveProject(ProjectSaveRequest projectSaveRequest, long memberId) {
        long projectId = projectRepository.save(projectSaveRequest.toEntity()).getProjectId();
        recruitmentRepository.saveAllRecruitments(projectSaveRequest.recruitmentRoles(), projectId);
        projectStackRepository.saveAllProjectStacks(projectSaveRequest.techStack(), projectId);
        projectMemberRepository.registerProjectLeader(projectId, memberId);
        return projectId;
    }


    @Transactional(readOnly = true)
    public List<ApplicationResponse> getApplicationsByProjectId(long projectId){
        long startTime = System.currentTimeMillis();
        log.info("getApplication 실행:");
        List<ProjectMemberResponse> projectMembers = projectMemberRepository.findApplicationsByProjectId(projectId);

        List<Long> memberIds = projectMembers.stream()
                .map(ProjectMemberResponse::projectMemberId)
                .toList();

        List<MemberStack> allTechStacks = memberStackRepository.findAllTechStacksByMemberIds(memberIds);

        Map<Long, List<MemberStackResponse>> techStacksByMemberId = allTechStacks.stream()
                .collect(Collectors.groupingBy(
                        ms -> ms.getMember().getMemberId(),
                        Collectors.mapping(
                                ms -> new MemberStackResponse(
                                        ms.getTechStack().getTechStackId() == 1 ? ms.getCustomStack() : ms.getTechStack().getName(),
                                        ms.getTechStack().getTechStackId() == 1 ? null : ms.getTechStack().getImgUrl()
                                ),
                                Collectors.toList()
                        )
                ));

        List<ApplicationResponse> result = projectMembers.stream()
                .map(pm -> {
                    log.info("findTechStackNames 실행:");
                    List<MemberStackResponse> techStacks = techStacksByMemberId.getOrDefault(pm.projectMemberId(), Collections.emptyList());
                    return ApplicationResponse.of(pm, techStacks);
                })
                .toList();
        long endTime = System.currentTimeMillis();
        log.info("getApplicationsByProjectId 쿼리 시간 : {} ms", (endTime - startTime));
        return result;
    }

    @Transactional
    public void updateAcceptStatus(UpdateAcceptStatusRequest updateAcceptStatusRequest, Long memberId){
        boolean isLeader = projectMemberRepository.isLeader(updateAcceptStatusRequest.projectId(), memberId);
        if(!isLeader) throw new CustomBaseException(ErrorCode.UNAUTHORIZED_ACTION);
        ProjectMember projectMember = projectMemberRepository.findByProjectIdAndMemberIdAndJobId(updateAcceptStatusRequest.projectId(), updateAcceptStatusRequest.jobId(), updateAcceptStatusRequest.memberId())
                .orElseThrow(()-> new NotFoundException("프로젝트 승인/거절 : 해당 유저를 찾을수 없습니다!"));
        projectMember.updateAcceptStatus(updateAcceptStatusRequest.acceptStatus()); // 변경감지 저장
        mqSender.sendMessage(MQExchange.ALARM.getExchange(), MQRouting.APPROVAL_RESULT.getRouting(), ProjectApprovalResultAlarm.from(projectMember));
        //log.info(projectMember.toString());
    }

    @Transactional
    public void projectStart(long projectId, Long memberId){
        boolean isLeader = projectMemberRepository.isLeader(projectId, memberId);
        if(!isLeader) throw new CustomBaseException(ErrorCode.UNAUTHORIZED_ACTION);
        projectRepository.findById(projectId)
                .orElseThrow(()->new NotFoundException("프로젝트 시작 : 해당 ProjecetId 와 일치하는 Project 가 없습니다."))
                .onGoing();
        projectMemberRepository.updateAcceptStatusByProjectId(projectId);
    }

    // 프로젝트 지원
    @Transactional
    public void applyProject(Long projectId, Long memberId, Long jobId) {
        boolean isApplication = projectMemberRepository.isApplication(projectId, memberId);
        if(isApplication) throw new CustomBaseException(ErrorCode.ALREADY_APPLICATION);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job not found"));

        // project_member 테이블에 데이터 삽입
        ProjectMember projectMember = ProjectMember.of(project, member, job);

        projectMemberRepository.save(projectMember);

        long leaderId = projectMemberRepository.findLeaderMemberIdByProjectId(projectId).orElseThrow(()->new NotFoundException("리더를 찾을수 없습니다."));

        // alarm 테이블에 데이터 삽입
        Alarm alarm = Alarm.of(project, member, job, 0);
        ProjectApplicationAlarm projectApplicationAlarm = ProjectApplicationAlarm.builder()
                .projectName(project.getName())
                .nickname(member.getNickname())
                .jobName(job.getName())
                .projectLeaderId(leaderId)
                .build();
        mqSender.sendMessage(MQExchange.ALARM.getExchange(), MQRouting.APPLICATION_CREATE.getRouting(), projectApplicationAlarm);
        alarmRepository.save(alarm);
    }


    //프로젝트 카드에 들어갈 내용 조회
    @Transactional(readOnly = true)
    public List<ProjectCardResponse> getProjectCardList(List<Project> projectList, Long memberId) {

        List<ProjectCardResponse> projectDocuments = new ArrayList<>();

        for(Project project : projectList){
            // 프로젝트 연관 기술 스택 정보
            List<ProjectStack> projectTechStacks = projectStackRepository.findAllByProject_ProjectId(project.getProjectId());

            //techStack list로 저장
            List<ProjectCardResponse.TechStack> techStackList= new ArrayList<>();
            for (ProjectStack projectStack : projectTechStacks) {
                Long techStackId = projectStack.getTechStack().getTechStackId();
                if(techStackId == 1){
                    techStackList.add(new ProjectCardResponse.TechStack(projectStack.getCustomStack(), null));
                }else{
                    Optional<TechStack> techStackOpt = techStackRepository.findById(projectStack.getTechStack().getTechStackId());
                    if (techStackOpt.isPresent()) {
                        TechStack techStack = techStackOpt.get();
                        String name = techStack.getName();
                        String imgUrl = techStack.getImgUrl();
                        techStackList.add(new ProjectCardResponse.TechStack(name, imgUrl));
                    }
                }

            }
            // 로그인한 사용자의 좋아요 여부만 확인
            boolean isLiked = (memberId != null) && projectLikeRepository.existsByProject_ProjectIdAndMember_MemberId(project.getProjectId(), memberId);

            // 좋아요 수
            Long cntLike = projectLikeRepository.countProjectLikeByProject_ProjectId(project.getProjectId());

            // 프로젝트에 필요한 인원
            Integer requiredMemberCount = recruitmentRepository.findMemberCountByProject_ProjectId(project.getProjectId());
            // 프로젝트에 지원한 인원
            Integer currentMemberCount = projectMemberRepository.findApprovedMemberCountByProject_ProjectId(project.getProjectId());


            ProjectCardResponse projectCard = ProjectCardResponse.builder()
                    .projectId(project.getProjectId())
                    .name(project.getName())
                    .imgUrl(project.getImgUrl())
                    .duration(project.getDuration())
                    .projectTechStack(techStackList)
                    .cntLike(cntLike)
                    .isLiked(isLiked)
                    .currentCnt(currentMemberCount)
                    .teamCnt(requiredMemberCount)
                    .build();
            projectDocuments.add(projectCard);
        }
        return projectDocuments;

    }
    // 사용자가 '좋아요' 누른 프로젝트 조회
    @Transactional(readOnly = true)
    public Page<ProjectCardResponse> getProjectsByMemberAndLike(Long memberId, Pageable pageable) {
        Page<Project> projectPage = projectRepository.findLikedProjectsByMemberId(memberId, pageable);
        List<ProjectCardResponse> projectCardList = getProjectCardList(projectPage.getContent(),memberId);
        return new PageImpl<>(projectCardList, pageable, projectPage.getTotalElements());
    }

    // 사용자의 프로젝트를 진행 상태별로 조회
    @Transactional(readOnly = true)
    public Page<ProjectCardResponse> getProjectsByMemberAndStatus(Long memberId, Integer status, Pageable pageable) {

        Page<Project> projectPage;

        // 모집 중인 프로젝트를 조회하는 경우 (status == 0)
        if (status == 0) {
            // 사용자가 리더인 프로젝트만 조회
            projectPage = projectRepository.findLeaderProjectsByMemberIdAndStatus(memberId, status, pageable);
        } else {
            // 일반적으로 memberId로 프로젝트 조회
            projectPage = projectRepository.findProjectsByMemberIdAndStatus(memberId, status, pageable);
        }
        List<ProjectCardResponse> projectCardList = getProjectCardList(projectPage.getContent(),memberId);
        return new PageImpl<>(projectCardList, pageable, projectPage.getTotalElements());

    }

    // 신규순으로 조회 (모집중, 진행중)
    public Page<ProjectCardResponse> getProjectsOrderedByCreatedAt(Pageable pageable,Long memberId) {

        Page<Project> projectPage = projectRepository.findAllByStatusOrderByCreatedAtDesc(0, pageable);
        List<ProjectCardResponse> projectCardList = getProjectCardList(projectPage.getContent(),memberId);
        return new PageImpl<>(projectCardList, pageable, projectPage.getTotalElements());
    }

    // 회고 조회
    public RetrospectiveResponse getRetrospective(Long projectId, Long memberId, int week) {

        return projectRepository.getRetrospective(projectId, memberId, week)
                .orElseThrow(() -> new CustomBaseException(ErrorCode.NOT_FOUND));
    }
    @Transactional
    public void registerProjectLike(long projectId, long memberId) {
        projectLikeRepository.registerProjectLike(projectId, memberId);
    }
    @Transactional
    public void deleteProjectLike(long projectId, long memberId) {
        projectLikeRepository.deleteProjectLike(projectId, memberId);
    }


    // 최근 종료된 순으로 조회
    public Page<ProjectCardResponse> getProjectsByDeadline(Pageable pageable,Long memberId) {
        // 1. 프로젝트 목록 가져오기
        Page<Project> projects = projectRepository.findAllCompletedProjects(pageable);

        // 2. 프로젝트를 마감일 기준으로 정렬
        List<Project> sortedProjects = projects.stream()
                .sorted(Comparator.comparing(Project::getDeadline).reversed())
                .collect(Collectors.toList());


        // 3. getProjectCardList 메서드를 사용하여 ProjectCardResponse 리스트로 변환
        List<ProjectCardResponse> projectCardResponses = getProjectCardList(sortedProjects,memberId);

        return new PageImpl<>(projectCardResponses, pageable, projects.getTotalElements());
    }

    // 인기 프로젝트 조회
    public List<PopularProjectResponse> getTop10PopularProjects() {

        List<PopularProjectResponse> projects = projectRepository.findPopularProjects(10 );
        return projectRepository.findPopularProjects(10);
    }


    // 시간 가중치 계산 (최근 5일을 1로 설정하고 점점 감소)
    private double calculateTimeWeight(LocalDate createdDate) {
        long daysSinceCreation = ChronoUnit.DAYS.between(createdDate, LocalDate.now());
        if (daysSinceCreation <= 5) {
            return 1.0; // 최근 5일 이내면 가중치 1
        }
        return Math.max(0.1, 1 - (double) daysSinceCreation / 30); // 30일 이후로 서서히 가중치 감소
    }


    // 프로젝트 회고 등록
    public void saveRetrospective(Long projectId, Long memberId, CreateRetrospectiveRequest createRetrospectiveRequest) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new CustomBaseException(ErrorCode.NOT_FOUND));
        log.debug("project: {}", project);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomBaseException(ErrorCode.NOT_FOUND));
        log.debug("member: {}", member);

        ProjectMember projectMember = projectMemberRepository.findByProjectIdAndMemberId(projectId, memberId)
                .orElseThrow(() -> new CustomBaseException(ErrorCode.NOT_FOUND));
        log.debug("projectMember: {}", projectMember);

        Job job = projectMember.getJob();

        Retrospective retrospective = Retrospective.of(project, member, job, createRetrospectiveRequest.title(),
                createRetrospectiveRequest.content(), createRetrospectiveRequest.week());
        log.debug("retrospective: {}", retrospective);

        retrospectiveRepository.save(retrospective);
    }
}
