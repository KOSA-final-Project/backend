package site.hesil.latteve_spring.domains.project.service;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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
import site.hesil.latteve_spring.domains.project.dto.project.response.ProjectDetailResponse;
import site.hesil.latteve_spring.domains.project.dto.request.projectSave.ProjectSaveRequest;
import site.hesil.latteve_spring.domains.project.dto.response.ApplicationResponse;
import site.hesil.latteve_spring.domains.project.repository.project.ProjectRepository;
import site.hesil.latteve_spring.domains.project.repository.projectMember.ProjectMemberRepository;
import site.hesil.latteve_spring.domains.project.repository.recruitment.RecruitmentRepository;
import site.hesil.latteve_spring.domains.projectStack.repository.ProjectStackRepository;
import site.hesil.latteve_spring.global.error.errorcode.ErrorCode;
import site.hesil.latteve_spring.global.error.exception.CustomBaseException;

import java.util.List;

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
 */

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


    // 프로젝트 상세 페이지 정보
    public ProjectDetailResponse getProjectDetail(Long projectId) {
        return projectRepository.getProjectDetail(projectId)
                .orElseThrow(() -> new CustomBaseException(ErrorCode.NOT_FOUND));
    }

    @Transactional
    public void saveProject(ProjectSaveRequest projectSaveRequest) {
        long projectId = projectRepository.save(projectSaveRequest.toEntity()).getProjectId();
        recruitmentRepository.saveAllRecruitments(projectSaveRequest.recruitmentRoles(), projectId);
        projectStackRepository.saveAllProjectStacks(projectSaveRequest.techStack(), projectId);
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
}
