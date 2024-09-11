package site.hesil.latteve_spring.domains.project.service;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hesil.latteve_spring.domains.alarm.domain.Alarm;
import site.hesil.latteve_spring.domains.alarm.dto.ProjectApplicationAlarm;
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
        ProjectMember projectMember = projectMemberRepository.findByProjectIdAndMemberId(updateAcceptStatusRequest.projectId(), updateAcceptStatusRequest.memberId())
                .orElseThrow(()-> new NotFoundException("프로젝트 승인/거절 : 해당 유저를 찾을수 없습니다!"));
        projectMember.updateAcceptStatus(updateAcceptStatusRequest.acceptStatus()); // 변경감지 저장
        log.info(projectMember.toString());
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
//    public List<PopularProjectResponse> getTop10PopularProjects() {
//        List<Project> projects = projectRepository.findAll(); // 프로젝트 데이터 전체 조회
//
//        // 좋아요 수 최대값 조회
//        Long maxLikes = projectLikeRepository.findMaxLikes();
//
//        return projects.stream()
//                .map(project -> {
//                    // 프로젝트의 직무별 모집 인원 및 지원자 수 계산
//                    List<Recruitment> recruitments = recruitmentRepository.findByProject(project);
//                    long totalRecruitmentCount = recruitments.stream().mapToInt(Recruitment::getCount).sum();
//
//                    // 모집 대비 지원자 수 계산
//                    long totalApplicantsCount = projectMemberRepository.countByProject(project);
//
//                    // 모집 대비 지원자 비율
//                    double normalizedApplicants = totalRecruitmentCount > 0
//                            ? (double) totalApplicantsCount / totalRecruitmentCount
//                            : 0;
//
//                    // 각 프로젝트의 좋아요 수 조회
//                    Long projectLikes = projectLikeRepository.countProjectLikeByProject_ProjectId(project.getProjectId());
//
//                    // 좋아요 수 정규화
//                    double normalizedLikes = maxLikes > 0
//                            ? (double) projectLikes / maxLikes
//                            : 0;
//
//                    // 시간 가중치 계산 (최신순)
//                    double weightTime = calculateTimeWeight(project.getCreatedAt().toLocalDate());
//
//                    // 최종 인기도 점수 계산
//                    double popularityScore = (normalizedLikes * 0.4) + (normalizedApplicants * 0.4) + (weightTime * 0.2);
//
//                    // PopularProjectResponse로 변환하여 반환
//                    return new PopularProjectResponse(
//                            project.getProjectId(),
//                            project.getName(),
//                            popularityScore,
//
//                })
//                .sorted(Comparator.comparingDouble(Project::getPopularityScore).reversed())
//                .limit(10)
//                .map(this::toPopularProjectResponse) // 필요한 DTO 변환
//                .collect(Collectors.toList());
//    }

    public List<PopularProjectResponse> getTop10PopularProjects() {
        List<Project> projects = projectRepository.findAll(); // 프로젝트 데이터 전체 조회

        return projects.stream()
                .map(project -> {
                    // 각 프로젝트의 좋아요 수 조회
                    Long projectLikes = projectLikeRepository.countProjectLikeByProject_ProjectId(project.getProjectId());

                    // 지원자 수 조회
                    long totalApplicantsCount = projectMemberRepository. findMemberCountByProject_ProjectId(project.getProjectId());

                    // 현재 모인 팀원수
                    long currentApprovedCnt = projectMemberRepository.findApprovedMemberCountByProject_ProjectId(project.getProjectId());

                    //총 모집 인원 조회
                    long totalRecruitmentCount = recruitmentRepository.findMemberCountByProject_ProjectId(project.getProjectId());

                    // 모집 인원 대비 지원자 비율 계산
                    double applicantsRatio = totalRecruitmentCount > 0
                            ? (double) totalApplicantsCount / totalRecruitmentCount
                            : 0;

                    // 좋아요 수와 모집 대비 지원자 비율 합산
                    double popularitySum = projectLikes + applicantsRatio;

                    // 기술 스택 정보 조회
                    List<ProjectStack> projectTechStacks = projectStackRepository.findAllByProject_ProjectId(project.getProjectId());
                    List<ProjectCardResponse.TechStack> techStackList = projectTechStacks.stream()
                            .map(stack -> {
                                Long techStackId = stack.getTechStack().getTechStackId();
                                if (techStackId == 1) {
                                    return new ProjectCardResponse.TechStack(stack.getCustomStack(), null);
                                } else {
                                    Optional<TechStack> techStackOpt = techStackRepository.findById(techStackId);
                                    return techStackOpt.map(techStack ->
                                                    new ProjectCardResponse.TechStack(techStack.getName(), techStack.getImgUrl()))
                                            .orElse(null);
                                }
                            })
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());

                    // 직무별 모집 정보 조회 및 변환
                    List<String> recruitmentNames = recruitmentRepository.findByProjectExcludingJobId1(project)
                            .stream()
                            .map(recruitment -> recruitment.getJob().getName())
                            .collect(Collectors.toList());

                    // PopularProjectResponse로 변환하여 반환
                    return PopularProjectResponse.builder()
                            .projectId(project.getProjectId())                      // 프로젝트 ID
                            .name(project.getName())                                // 프로젝트 이름
                            .imgUrl(project.getImgUrl())                            // 이미지 URL
                            .projectTechStack(techStackList)                        // 프로젝트 기술 스택 (List<ProjectCardResponse.TechStack>)
                            .description(project.getDescription())                  // 프로젝트 설명
                            .recruitmentName(recruitmentNames)
                            .duration(project.getDuration())
                            .deadline(project.getDeadline())                        // 프로젝트 마감일 (주차)
                            .createdAt(project.getCreatedAt())
                            .cntLike(projectLikes)                                  // 좋아요 수
                            .currentCnt((int) currentApprovedCnt)                 // 현재 모인 팀원 수
                            .teamCnt((int) totalRecruitmentCount)                   // 총 팀원 수
                            .popularityScore(popularitySum)                         // 인기 점수 (좋아요 + 모집 대비 지원자 비율)
                            .build();
                })
                // 좋아요 수 + 모집 대비 지원자 비율로 내림차순 정렬
                .sorted(Comparator.comparingDouble(PopularProjectResponse::popularityScore).reversed()
                        // 동일한 경우 최신순으로 정렬
                        .thenComparing(PopularProjectResponse::createdAt, Comparator.reverseOrder()))
                .limit(10)  // 상위 10개
                .collect(Collectors.toList());
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
