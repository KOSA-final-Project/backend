package site.hesil.latteve_spring.domains.project.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hesil.latteve_spring.domains.alarm.repository.AlarmRepository;
import site.hesil.latteve_spring.domains.project.dto.request.projectSave.ProjectSaveRequest;
import site.hesil.latteve_spring.domains.project.repository.project.ProjectRepository;
import site.hesil.latteve_spring.domains.project.repository.recruitment.RecruitmentRepository;
import site.hesil.latteve_spring.domains.projectStack.repository.ProjectStackRepository;

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
 * 2024-09-14        Yeong-Huns    프로젝트 지원시, 시작시, alarm 테이블 업데이트
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService {
//
    private final ProjectRepository projectRepository;
//    private final ProjectMemberRepository projectMemberRepository;
//    private final MemberRepository memberRepository;
//    private final JobRepository jobRepository;
    private final AlarmRepository alarmRepository;
    private final ProjectStackRepository projectStackRepository;
    private final RecruitmentRepository recruitmentRepository;
//    private final MemberStackRepository memberStackRepository;
//    private final TechStackRepository techStackRepository;
//    private final ProjectLikeRepository projectLikeRepository;
//    private final RetrospectiveRepository retrospectiveRepository;
//    private final MQSender mqSender;
//    private final ApplicationEventPublisher eventPublisher;
//
//    // 프로젝트 상세 페이지 정보
//    @Transactional(readOnly = true)
//    public ProjectDetailResponse getProjectDetail(Long projectId) {
//        long startTime = System.currentTimeMillis();
//        ProjectDetailResponse result = projectRepository.getProjectDetail_deprecated(projectId)
//                .orElseThrow(() -> new CustomBaseException(ErrorCode.NOT_FOUND));
//        long endTime = System.currentTimeMillis();
//        log.info("getProjectDetail 쿼리 시간 : {} ms", (endTime - startTime));
//        return result;
//    }
//
    @Transactional
    public long saveProject(ProjectSaveRequest projectSaveRequest, long memberId) {
        long projectId = projectRepository.save(projectSaveRequest.toEntity()).getProjectId();
        recruitmentRepository.saveAllRecruitments(projectSaveRequest.recruitmentRoles(), projectId);
        projectStackRepository.saveAllProjectStacks(projectSaveRequest.techStack(), projectId);
        alarmRepository.registerProjectLeader(projectId ,memberId);
        return projectId;
    }
//
//
//    @Transactional(readOnly = true)
//    public List<ApplicationResponse> getApplicationsByProjectId(long projectId) {
//        long startTime = System.currentTimeMillis();
//        log.info("getApplication 실행:");
//        List<ProjectMemberResponse> projectMembers = projectMemberRepository.findApplicationsByProjectId(projectId);
//
//        List<Long> memberIds = projectMembers.stream()
//                .map(ProjectMemberResponse::projectMemberId)
//                .toList();
//
//        List<MemberStack> allTechStacks = memberStackRepository.findAllTechStacksByMemberIds(memberIds);
//
//        Map<Long, List<MemberStackResponse>> techStacksByMemberId = allTechStacks.stream()
//                .collect(Collectors.groupingBy(
//                        ms -> ms.getMember().getMemberId(),
//                        Collectors.mapping(
//                                ms -> new MemberStackResponse(
//                                        ms.getTechStack().getTechStackId() == 1 ? ms.getCustomStack() : ms.getTechStack().getName(),
//                                        ms.getTechStack().getTechStackId() == 1 ? null : ms.getTechStack().getImgUrl()
//                                ),
//                                Collectors.toList()
//                        )
//                ));
//
//        List<ApplicationResponse> result = projectMembers.stream()
//                .map(pm -> {
//                    log.info("findTechStackNames 실행:");
//                    List<MemberStackResponse> techStacks = techStacksByMemberId.getOrDefault(pm.projectMemberId(), Collections.emptyList());
//                    return ApplicationResponse.of(pm, techStacks);
//                })
//                .toList();
//        long endTime = System.currentTimeMillis();
//        log.info("getApplicationsByProjectId 쿼리 시간 : {} ms", (endTime - startTime));
//        return result;
//    }
//
//    @Transactional
//    public void updateAcceptStatus(UpdateAcceptStatusRequest updateAcceptStatusRequest, Long memberId) {
//        boolean isLeader = projectMemberRepository.isLeader(updateAcceptStatusRequest.projectId(), memberId);
//        if (!isLeader) throw new CustomBaseException(ErrorCode.UNAUTHORIZED_ACTION);
//        ProjectJob projectJob = projectMemberRepository.findByProjectIdAndMemberIdAndJobId(updateAcceptStatusRequest.projectId(), updateAcceptStatusRequest.jobId(), updateAcceptStatusRequest.memberId())
//                .orElseThrow(() -> new NotFoundException("프로젝트 승인/거절 : 해당 유저를 찾을수 없습니다!"));
//
//        log.info("==============================projectService:  member update before");
//        projectJob.updateAcceptStatus(updateAcceptStatusRequest.acceptStatus()); // 변경감지 저장
//
//        log.info("==============================projectService:  member update after");
//        Alarm alarm = alarmRepository.findAlarmByProjectIdAndMemberId(updateAcceptStatusRequest.projectId(), updateAcceptStatusRequest.memberId())
//                .orElseThrow(() -> new NotFoundException("해당 프로젝트ID, memberID로 등록된 지원 알람이 없습니다."));
//        int alarmResult = updateAcceptStatusRequest.acceptStatus() == 1 ? 1 : 2;
//        alarm.updateAcceptStatus(alarmResult); // 변경감지 저장
//        mqSender.sendMessage(MQExchange.ALARM.getExchange(), "user."+ projectJob.getMember().getMemberId(), ProjectApprovalResultAlarm.from(projectJob));
//        String result = alarmResult == 1 ? "승인" : "거절";
//        log.info("프로젝트 지원 결과 : {}", result);
//    }
//
//
//    @Transactional
//    public void projectStart(long projectId, Long memberId) {
//        boolean isLeader = projectMemberRepository.isLeader(projectId, memberId);
//        if (!isLeader) throw new CustomBaseException(ErrorCode.UNAUTHORIZED_ACTION);
//        projectRepository.findById(projectId)
//                .orElseThrow(() -> new NotFoundException("프로젝트 시작 : 해당 ProjecetId 와 일치하는 Project 가 없습니다."))
//                .onGoing();
//        projectMemberRepository.findByProjectIdAndNotAccept(projectId).forEach(pm->{
//            pm.updateAcceptStatus(2);
//            mqSender.sendMessage(MQExchange.ALARM.getExchange(), "user."+pm.getMember().getMemberId(), ProjectApprovalResultAlarm.from(pm));
//        });
//        /*projectMemberRepository.updateAcceptStatusByProjectId(projectId);*/
//        alarmRepository.updateTypeByProjectId(projectId);
//    }
//
//    // 프로젝트 지원
//    @Transactional
//    public void applyProject(Long projectId, Long memberId, Long jobId) {
//        boolean isApplication = projectMemberRepository.isApplication(projectId, memberId);
//        if (isApplication) throw new CustomBaseException(ErrorCode.ALREADY_APPLICATION);
//
//        Project project = projectRepository.findById(projectId)
//                .orElseThrow(() -> new EntityNotFoundException("Project not found"));
//
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new EntityNotFoundException("Member not found"));
//
//        Job job = jobRepository.findById(jobId)
//                .orElseThrow(() -> new EntityNotFoundException("Job not found"));
//
//        // project_member 테이블에 데이터 삽입
//        ProjectJob projectJob = ProjectJob.of(project, member, job);
//
//        projectMemberRepository.save(projectJob);
//
//        long leaderId = projectMemberRepository.findLeaderMemberIdByProjectId(projectId).orElseThrow(() -> new NotFoundException("리더를 찾을수 없습니다."));
//
//        // alarm 테이블에 데이터 삽입
//        Alarm alarm = Alarm.of(project, member, job, 0);
//        ProjectApplicationAlarm projectApplicationAlarm = ProjectApplicationAlarm.builder()
//                .projectName(project.getName())
//                .memberId(member.getMemberId())
//                .imgUrl(member.getImgUrl())
//                .nickname(member.getNickname())
//                .jobName(job.getName())
//                .projectLeaderId(leaderId)
//                .type("application")
//                .build();
//        mqSender.sendMessage(MQExchange.ALARM.getExchange(), "user."+projectApplicationAlarm.projectLeaderId(), projectApplicationAlarm);
//        alarmRepository.save(alarm);
//    }
//
//
//    //프로젝트 카드에 들어갈 내용 조회
//    @Transactional(readOnly = true)
//    public List<ProjectCardResponse> getProjectCardList(List<Project> projectList, Long memberId) {
//
//        List<ProjectCardResponse> projectDocuments = new ArrayList<>();
//
//        for (Project project : projectList) {
//            // 프로젝트 연관 기술 스택 정보
//            List<ProjectStack> projectTechStacks = projectStackRepository.findAllByProject_ProjectId(project.getProjectId());
//
//            //techStack list로 저장
//            List<ProjectCardResponse.TechStack> techStackList = new ArrayList<>();
//            for (ProjectStack projectStack : projectTechStacks) {
//                Long techStackId = projectStack.getTechStack().getTechStackId();
//                if (techStackId == 1) {
//                    techStackList.add(new ProjectCardResponse.TechStack(projectStack.getCustomStack(), null));
//                } else {
//                    Optional<TechStack> techStackOpt = techStackRepository.findById(projectStack.getTechStack().getTechStackId());
//                    if (techStackOpt.isPresent()) {
//                        TechStack techStack = techStackOpt.get();
//                        String name = techStack.getName();
//                        String imgUrl = techStack.getImgUrl();
//                        techStackList.add(new ProjectCardResponse.TechStack(name, imgUrl));
//                    }
//                }
//
//            }
//            // 로그인한 사용자의 좋아요 여부만 확인
//            boolean isLiked = (memberId != null) && projectLikeRepository.existsByProject_ProjectIdAndMember_MemberId(project.getProjectId(), memberId);
//
//            // 좋아요 수
//            Long cntLike = projectLikeRepository.countProjectLikeByProject_ProjectId(project.getProjectId());
//
//            // 프로젝트에 필요한 인원
//            Integer requiredMemberCount = recruitmentRepository.findMemberCountByProject_ProjectId(project.getProjectId());
//            // 프로젝트에 지원한 인원
//            Integer currentMemberCount = projectMemberRepository.findApprovedMemberCountByProject_ProjectId(project.getProjectId());
//
//
//            ProjectCardResponse projectCard = ProjectCardResponse.builder()
//                    .projectId(project.getProjectId())
//                    .name(project.getName())
//                    .imgUrl(project.getImgUrl())
//                    .duration(project.getDuration())
//                    .projectTechStack(techStackList)
//                    .cntLike(cntLike)
//                    .isLiked(isLiked)
//                    .currentCnt(currentMemberCount)
//                    .teamCnt(requiredMemberCount)
//                    .build();
//            projectDocuments.add(projectCard);
//        }
//        return projectDocuments;
//
//    }
//
//    // 사용자가 '좋아요' 누른 프로젝트 조회
//    @Transactional(readOnly = true)
//    public Page<ProjectCardResponse> getProjectsByMemberAndLike(Long memberId, Pageable pageable) {
//        Page<Project> projectPage = projectRepository.findLikedProjectsByMemberId(memberId, pageable);
//        List<ProjectCardResponse> projectCardList = getProjectCardList(projectPage.getContent(), memberId);
//        return new PageImpl<>(projectCardList, pageable, projectPage.getTotalElements());
//    }
//
//    // 사용자의 프로젝트를 진행 상태별로 조회
//    @Transactional(readOnly = true)
//    public Page<ProjectCardResponse> getProjectsByMemberAndStatus(Long memberId, Integer status, Pageable pageable) {
//
//        Page<Project> projectPage;
//
//        // 모집 중인 프로젝트를 조회하는 경우 (status == 0)
//        if (status == 0) {
//            // 사용자가 리더인 프로젝트만 조회
//            projectPage = projectRepository.findLeaderProjectsByMemberIdAndStatus(memberId, status, pageable);
//        } else {
//            // 일반적으로 memberId로 프로젝트 조회
//            projectPage = projectRepository.findProjectsByMemberIdAndStatus(memberId, status, pageable);
//        }
//        List<ProjectCardResponse> projectCardList = getProjectCardList(projectPage.getContent(), memberId);
//        return new PageImpl<>(projectCardList, pageable, projectPage.getTotalElements());
//
//    }
//
//    // 신규순으로 조회 (모집중, 진행중)
//    public Page<ProjectCardResponse> getProjectsOrderedByCreatedAt(Pageable pageable, Long memberId) {
//
//        Page<Project> projectPage = projectRepository.findAllByStatusOrderByCreatedAtDesc(0, pageable);
//        List<ProjectCardResponse> projectCardList = getProjectCardList(projectPage.getContent(), memberId);
//        return new PageImpl<>(projectCardList, pageable, projectPage.getTotalElements());
//    }
//
//    // 회고 조회
//    public RetrospectiveResponse getRetrospective(Long projectId, Long memberId, int week) {
//
//        return projectRepository.getRetrospective(projectId, memberId, week)
//                .orElseThrow(() -> new CustomBaseException(ErrorCode.NOT_FOUND));
//    }
//
//    @Transactional
//    public void registerProjectLike(long projectId, long memberId) {
//        // 프로젝트와 멤버를 각각 찾아서 ProjectLike를 생성
//        Project project = projectRepository.findById(projectId)
//                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
//
//        ProjectLike projectLike = ProjectLike.builder()
//                .project(project)
//                .member(member).build();
//
//        // JPA의 save() 메서드로 저장
//        projectLikeRepository.save(projectLike);
////        projectLikeRepository.registerProjectLike(projectId, memberId);
//    }
//
//    @Transactional
//    public void deleteProjectLike(long projectId, long memberId) {
//        projectLikeRepository.deleteProjectLike(projectId, memberId);
//    }


    // 최근 종료된 순으로 조회
//    public Page<ProjectCardResponse> getProjectsByDeadline(Pageable pageable, Long memberId) {
//        // 1. 프로젝트 목록 가져오기
//        Page<Project> projects = projectRepository.findAllCompletedProjects(pageable);
//
//        // 2. 프로젝트를 마감일 기준으로 정렬
//        List<Project> sortedProjects = projects.stream()
//                .sorted(Comparator.comparing(Project::getDeadline).reversed())
//                .collect(Collectors.toList());
//
//
//        // 3. getProjectCardList 메서드를 사용하여 ProjectCardResponse 리스트로 변환
//        List<ProjectCardResponse> projectCardResponses = getProjectCardList(sortedProjects, memberId);
//
//        return new PageImpl<>(projectCardResponses, pageable, projects.getTotalElements());
//    }

//    // 인기 프로젝트 조회
//    public List<PopularProjectResponse> getTop10PopularProjects() {
//
//        List<PopularProjectResponse> projects = projectRepository.findPopularProjects(10);
//        return projectRepository.findPopularProjects(10);
//    }


//
//    // 프로젝트 회고 등록
//    public void saveRetrospective(Long projectId, Long memberId, CreateRetrospectiveRequest createRetrospectiveRequest) {
//
//        Project project = projectRepository.findById(projectId)
//                .orElseThrow(() -> new CustomBaseException(ErrorCode.NOT_FOUND));
//        log.debug("project: {}", project);
//
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new CustomBaseException(ErrorCode.NOT_FOUND));
//        log.debug("member: {}", member);
//
//        ProjectJob projectJob = projectMemberRepository.findByProjectIdAndMemberId(projectId, memberId)
//                .orElseThrow(() -> new CustomBaseException(ErrorCode.NOT_FOUND));
//        log.debug("projectMember: {}", projectJob);
//
//        Job job = projectJob.getJob();
//
//        Retrospective retrospective = Retrospective.of(project, member, job, createRetrospectiveRequest.title(),
//                createRetrospectiveRequest.content(), createRetrospectiveRequest.week());
//        log.debug("retrospective: {}", retrospective);
//
//        retrospectiveRepository.save(retrospective);
//    }
//
//    // 프로젝트 회고 수정
//    public void updateRetrospective(Long retrospectiveId, UpdateRetrospectiveRequest updateRetrospectiveRequest) {
//
//        Retrospective retrospective = retrospectiveRepository.findById(retrospectiveId)
//                .orElseThrow(() -> new CustomBaseException(ErrorCode.NOT_FOUND));
//
//        retrospective.update(updateRetrospectiveRequest.title(), updateRetrospectiveRequest.content());
//
//        retrospectiveRepository.save(retrospective);
//    }
//
//    // 프로젝트 지원 여부 확인
//    public boolean isApplication(Long projectId, Long memberId) {
//        return projectMemberRepository.isApplication(projectId, memberId);
//    }
}
