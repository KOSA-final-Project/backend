package site.hesil.latteve_spring.domains.project.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.hesil.latteve_spring.domains.memberStack.repository.MemberStackRepository;
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
    private final ProjectStackRepository projectStackRepository;
    private final RecruitmentRepository recruitmentRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final MemberStackRepository memberStackRepository;


    public ProjectDetailResponse getProjectDetail(Long projectId) {
        ProjectDetailResponse projectDetailResponse = projectRepository.getProjectDetail(projectId);
        if (projectDetailResponse == null) {
            throw new CustomBaseException(ErrorCode.NOT_FOUND);
        }
        return projectDetailResponse;
    } // 이 부분 처음부터 Optional 로 받아오면 깔끔하지 않을까요 ? -YH

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
}
