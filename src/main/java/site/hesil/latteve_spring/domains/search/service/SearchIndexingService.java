package site.hesil.latteve_spring.domains.search.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch.core.IndexRequest;
import org.springframework.stereotype.Service;
import site.hesil.latteve_spring.domains.job.domain.Job;
import site.hesil.latteve_spring.domains.job.repository.JobRepository;
import site.hesil.latteve_spring.domains.member.domain.Member;
import site.hesil.latteve_spring.domains.member.domain.memberJob.MemberJob;
import site.hesil.latteve_spring.domains.member.repository.MemberRepository;
import site.hesil.latteve_spring.domains.member.repository.memberJob.MemberJobRepository;
import site.hesil.latteve_spring.domains.memberStack.domain.MemberStack;
import site.hesil.latteve_spring.domains.memberStack.repository.MemberStackRepository;
import site.hesil.latteve_spring.domains.project.domain.Project;
import site.hesil.latteve_spring.domains.project.repository.project.ProjectRepository;
import site.hesil.latteve_spring.domains.project.repository.projectLike.ProjectLikeRepository;
import site.hesil.latteve_spring.domains.project.repository.projectMember.ProjectMemberRepository;
import site.hesil.latteve_spring.domains.project.repository.recruitment.RecruitmentRepository;
import site.hesil.latteve_spring.domains.projectStack.domain.ProjectStack;
import site.hesil.latteve_spring.domains.projectStack.repository.ProjectStackRepository;
import site.hesil.latteve_spring.domains.search.dto.member.request.MemberDocumentReq;
import site.hesil.latteve_spring.domains.search.dto.project.request.ProjectDocumentReq;
import site.hesil.latteve_spring.domains.techStack.domain.TechStack;
import site.hesil.latteve_spring.domains.techStack.repository.TechStackRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * packageName    : site.hesil.latteve_spring.domains.search.service
 * fileName       : SearchIndexingService
 * author         : Heeseon
 * date           : 2024-08-30
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-30        Heeseon       최초 생성
 */
@Service
@RequiredArgsConstructor
public class SearchIndexingService {

    private final OpenSearchClient openSearchClient;
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final MemberStackRepository memberStackRepository;
    private final ProjectStackRepository projectStackRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final RecruitmentRepository recruitmentRepository;
    private final TechStackRepository techStackRepository;
    private final MemberJobRepository memberJobRepository;
    private final JobRepository jobRepository;
    private final ProjectLikeRepository projectLikeRepository;

    @Transactional
    public void indexProjectsToOpenSearch() throws IOException {
        List<Project> projects = projectRepository.findAll();
        //Project -> Index
        for(Project project : projects) {
            // 프로젝트에 연관된 기술 스택  정보 가져옴
            List<ProjectStack> projectTechStacks = projectStackRepository.findAllByProject_ProjectId(project.getProjectId());

            // techStack list로 저장
            List<ProjectDocumentReq.TechStack> techStackList= new ArrayList<>();
            for (ProjectStack projectStack : projectTechStacks) {
                Optional<TechStack> techStackOpt = techStackRepository.findById(projectStack.getTechStack().getTechStackId());
                if (techStackOpt.isPresent()) {
                    TechStack techStack = techStackOpt.get();
                    String name = techStack.getName();
                    String imgUrl = techStack.getImgUrl() != null ? techStack.getImgUrl() :
                            (projectStack.getCustomStack() != null ? projectStack.getCustomStack() : name);
                    techStackList.add(new ProjectDocumentReq.TechStack(name, imgUrl));
                }
            }

            String statusToString = convertStatusToString(project.getStatus());


            // 좋아요 수
            Long cntLike = projectLikeRepository.countProjectLikeByProject_ProjectId(project.getProjectId());

            // 프로젝트에 필요한 인원
            Integer requiredMemberCount = recruitmentRepository.findMemberCountByProject_ProjectId(project.getProjectId());
            // 프로젝트에 지원한 인원
            Integer currentMemberCount = projectMemberRepository.findMemberCountByProject_ProjectId(project.getProjectId());

            // ProjectDocumentReq 생성
            ProjectDocumentReq projectDocumentReq = ProjectDocumentReq.builder()
                    .projectId(project.getProjectId())
                    .name(project.getName())
                    .imgUrl(project.getImgUrl())
                    .duration(project.getDuration())
                    .projectTechStack(techStackList)
                    .teamCnt(requiredMemberCount)
                    .currentCnt(currentMemberCount)
                    .cntLike(cntLike)
                    .status(statusToString)
                    .createdAt( formatLocalDateTime(project.getCreatedAt()))
                    .build();

            // Elasticsearch에 인덱싱
            IndexRequest<ProjectDocumentReq> indexRequest = new IndexRequest.Builder<ProjectDocumentReq>()
                    .index("projects")
                    .id(project.getProjectId().toString())
                    .document(projectDocumentReq)
                    .build();
            openSearchClient.index(indexRequest);
        }
    }
    public String formatLocalDateTime(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        return localDateTime.format(formatter);
    }

    private String convertStatusToString(int status) {
        switch (status) {
            case 0: return "모집중";
            case 1: return "진행중";
            case 2: return "종료";
            default: return "Unknown";
        }
    }


    @Transactional
    public void indexMembersToOpenSearch() throws IOException {
        List<Member> members = memberRepository.findAll();
        // Member -> Index
        for (Member member : members) {
            // 멤버에 연관된 기술 스택 정보 가져옴
            List<MemberStack> memberStacks = memberStackRepository.findAllByMember_MemberId(member.getMemberId());
            // techStack list로 저장
            List<MemberDocumentReq.TechStack> techStackList = new ArrayList<>();
            for (MemberStack memberStack : memberStacks) {
                Optional<TechStack> techStackOpt = techStackRepository.findById(memberStack.getTechStack().getTechStackId());
                if (techStackOpt.isPresent()) {
                    TechStack techStack = techStackOpt.get();
                    String name = techStack.getName();
                    String imgUrl = techStack.getImgUrl() != null ? techStack.getImgUrl() : (memberStack.getCustomStack() != null ? memberStack.getCustomStack() : name);

                    // TechStack 객체를 리스트에 추가
                    techStackList.add(new MemberDocumentReq.TechStack(name, imgUrl));
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

            // Member가 참여한 프로젝트 개수
            int ongoingProjectCount = projectRepository.countProjectsByMemberIdAndStatus(member.getMemberId(), 1);
            int completedProjectCount = projectRepository.countProjectsByMemberIdAndStatus(member.getMemberId(), 2);

            // careerSortValue 계산
            int careerSortValue = calculateCareerSortValue(Collections.singletonList(member.getCareer()));

            // MemberDocumentReq 생성
            MemberDocumentReq memberDocumentReq = MemberDocumentReq.builder()
                    .memberId(member.getMemberId())
                    .memberNickname(member.getNickname())
                    .memberImg(member.getImgUrl())
                    .memberGithub(member.getGithub())
                    .techStack(techStackList)  // TechStack 리스트 전달
                    .ongoingProjectCount(ongoingProjectCount)
                    .completedProjectCount(completedProjectCount)
                    .memberJob(jobList)
                    .career(member.getCareer())
                    .careerSortValue(careerSortValue)
                    .createdAt(formatLocalDateTime(member.getCreatedAt()))
                    .build();

            // Elasticsearch에 인덱싱
            IndexRequest<MemberDocumentReq> indexRequest = new IndexRequest.Builder<MemberDocumentReq>()
                    .index("members")
                    .id(member.getMemberId().toString())
                    .document(memberDocumentReq)
                    .build();
            openSearchClient.index(indexRequest);
        }
    }

    private int calculateCareerSortValue(List<String> career) {
        // 경력 직무에 따라 정수 값 설정 (예시: 시니어 3, 주니어 2, 신입 1)
        if (career.contains("시니어")) {
            return 3;
        } else if (career.contains("주니어")) {
            return 2;
        } else if (career.contains("신입")) {
            return 1;
        }
        return 0; // 기타 또는 알 수 없는 경우
    }
}
