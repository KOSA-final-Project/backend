package site.hesil.latteve_spring.domains.project.service;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hesil.latteve_spring.domains.alarm.domain.Alarm;
import site.hesil.latteve_spring.domains.alarm.repository.AlarmRepository;
import site.hesil.latteve_spring.domains.job.domain.Job;
import site.hesil.latteve_spring.domains.job.repository.JobRepository;
import site.hesil.latteve_spring.domains.member.domain.Member;
import site.hesil.latteve_spring.domains.member.repository.MemberRepository;
import site.hesil.latteve_spring.domains.memberStack.repository.MemberStackRepository;
import site.hesil.latteve_spring.domains.project.domain.Project;
import site.hesil.latteve_spring.domains.project.domain.projectMember.ProjectMember;
import site.hesil.latteve_spring.domains.project.dto.project.response.ProjectCardResponse;
import site.hesil.latteve_spring.domains.project.dto.project.response.ProjectDetailResponse;
import site.hesil.latteve_spring.domains.project.dto.request.projectSave.ProjectSaveRequest;
import site.hesil.latteve_spring.domains.project.dto.response.ApplicationResponse;
import site.hesil.latteve_spring.domains.project.repository.project.ProjectRepository;
import site.hesil.latteve_spring.domains.project.repository.projectLike.ProjectLikeRepository;
import site.hesil.latteve_spring.domains.project.repository.projectMember.ProjectMemberRepository;
import site.hesil.latteve_spring.domains.project.repository.recruitment.RecruitmentRepository;
import site.hesil.latteve_spring.domains.projectStack.domain.ProjectStack;
import site.hesil.latteve_spring.domains.projectStack.repository.ProjectStackRepository;

import site.hesil.latteve_spring.domains.techStack.domain.TechStack;
import site.hesil.latteve_spring.domains.techStack.repository.TechStackRepository;
import site.hesil.latteve_spring.global.error.errorcode.ErrorCode;
import site.hesil.latteve_spring.global.error.exception.CustomBaseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    // 프로젝트 상세 페이지 정보
    public ProjectDetailResponse getProjectDetail(Long projectId) {
        return projectRepository.getProjectDetail(projectId)
                .orElseThrow(() -> new CustomBaseException(ErrorCode.NOT_FOUND));
    }

    @Transactional
    public void saveProject(ProjectSaveRequest projectSaveRequest, long memberId) {
        long projectId = projectRepository.save(projectSaveRequest.toEntity()).getProjectId();
        recruitmentRepository.saveAllRecruitments(projectSaveRequest.recruitmentRoles(), projectId);
        projectStackRepository.saveAllProjectStacks(projectSaveRequest.techStack(), projectId);
        projectMemberRepository.registerProjectLeader(projectId, memberId);
    }


    @Transactional(readOnly = true)
    public List<ApplicationResponse> getApplicationsByProjectId(long projectId){
        return projectMemberRepository.findByProjectId(projectId).stream()
                .map(pm->{
                    List<String> techStacks = memberStackRepository.findTechStackNamesByMemberId(pm.getMember().getMemberId());
                    return ApplicationResponse.of(pm.getMember().getMemberId(), pm.getJob().getName(), techStacks);
                }).toList();
    }
    
  // 프로젝트 지원
    public void applyProject(Long projectId, Long memberId, Long jobId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job not found"));

        // project_member 테이블에 데이터 삽입
        ProjectMember projectMember = ProjectMember.createMember(project, member, job);

        projectMemberRepository.save(projectMember);

        // alarm 테이블에 데이터 삽입
        Alarm alarm = Alarm.createAlarm(project, member, job, 0);

        alarmRepository.save(alarm);

    }


    //프로젝트 카드에 들어갈 내용 조회
    @Transactional(readOnly = true)
    public List<ProjectCardResponse> getProjectDocuments(List<Project> projectList) {
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


            // 좋아요 수
            Long cntLike = projectLikeRepository.countProjectLikeByProject_ProjectId(project.getProjectId());

            // 프로젝트에 필요한 인원
            Integer requiredMemberCount = recruitmentRepository.findMemberCountByProject_ProjectId(project.getProjectId());
            // 프로젝트에 지원한 인원
            Integer currentMemberCount = projectMemberRepository.findMemberCountByProject_ProjectId(project.getProjectId());


            ProjectCardResponse projectCard = ProjectCardResponse.builder()
                    .projectId(project.getProjectId())
                    .name(project.getName())
                    .imgUrl(project.getImgUrl())
                    .duration(project.getDuration())
                    .projectTechStack(techStackList)
                    .cntLike(cntLike)
                    .currentCnt(currentMemberCount)
                    .teamCnt(currentMemberCount)
                    .build();
            projectDocuments.add(projectCard);
        }
        return projectDocuments;

    }
    // 사용자가 '좋아요' 누른 프로젝트 조회
    @Transactional(readOnly = true)
    public List<ProjectCardResponse> getProjectsByMemberAndLike(Long memberId){
        List<Project> projectList =  projectRepository.findLikedProjectsByMemberId(memberId);
        return getProjectDocuments(projectList);


    }
    // 사용자의 프로젝트를 진행 상태별로 조회
    @Transactional(readOnly = true)
    public List<ProjectCardResponse> getProjectsByMemberAndStatus(Long memberId, Integer status){
       List<Project > projectList = projectRepository.findProjectsByMemberIdAndStatus(memberId, status);
        return getProjectDocuments(projectList);
    }



}
