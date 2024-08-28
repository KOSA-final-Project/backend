package site.hesil.latteve_spring.domains.search.service;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch.core.IndexRequest;
import org.opensearch.client.opensearch.core.SearchRequest;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.springframework.stereotype.Service;
import site.hesil.latteve_spring.domains.member.repository.MemberRepository;
import site.hesil.latteve_spring.domains.project.domain.Project;
import site.hesil.latteve_spring.domains.project.repository.projectmember.ProjectMemberRepository;
import site.hesil.latteve_spring.domains.project.repository.project.ProjectRepository;
import site.hesil.latteve_spring.domains.project.repository.recruitment.RecruitmentRepository;
import site.hesil.latteve_spring.domains.projectStack.domain.ProjectStack;
import site.hesil.latteve_spring.domains.projectStack.repository.ProjectStackRepository;
import site.hesil.latteve_spring.domains.search.dto.project.request.ProjectDocumentReq;
import site.hesil.latteve_spring.domains.techStack.domain.TechStack;
import site.hesil.latteve_spring.domains.techStack.repository.TechStackRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * packageName    : site.hesil.latteve_spring.domains.search.service
 * fileName       : SearchProjectService
 * author         : Heeseon
 * date           : 2024-08-28
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-28        Heeseon       최초 생성
 */
@Service
@RequiredArgsConstructor
public class SearchService {
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final OpenSearchClient openSearchClient;
    private final ProjectStackRepository projectStackRepository;
    private final TechStackRepository techStackRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final RecruitmentRepository recruitmentRepository;


    // 나중에 삭제 -> 데이터 동기화
    @PostConstruct
    public void init() throws IOException {
        indexProjectsToOpenSearch();
    }


    public void indexProjectsToOpenSearch() throws IOException {
        List<Project> projects = projectRepository.findAll();
        //Project -> Index
        for(Project project : projects) {
            // 프로젝트에 연관된 기술 스택  정보 가져옴
            List<ProjectStack> projectTechStacks = projectStackRepository.findAllByProject_ProjectId(project.getProjectId());
            // 기술 스택 이미지 저장
            List<String> techStackImgUrls= projectTechStacks.stream()
                    .map(projectStack -> techStackRepository.findById(projectStack.getTechStack().getTechStackId()))
                    .filter(Optional::isPresent)  // Optional이 비어있지 않은 경우에만 처리
                    .map(Optional::get)
                    .map(TechStack::getImgUrl)
                    .toList();

            // 프로젝트에 필요한 인원
            Integer requiredMemberCount = recruitmentRepository.findMemberCountByProject_ProjectId(project.getProjectId());
            // 프로젝트에 지원한 인원
            Integer currentMemberCount = projectMemberRepository.findMemberCountByProject_ProjectId(project.getProjectId());


            // ProjectDocument 생성
            ProjectDocumentReq projectDocumentReq = ProjectDocumentReq.builder()
                    .name(project.getName())
                    .imgUrl(project.getImgUrl()) // 대표 이미지
                    .duration(project.getDuration())
                    .projectTechStack(techStackImgUrls)
                    .teamCnt(requiredMemberCount)
                    .currentCnt(currentMemberCount)
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

    public List<ProjectDocumentReq> searchProjectsByKeyword(String keyword) throws IOException {
        SearchRequest searchRequest = new SearchRequest.Builder()
                .index("projects")
                .query(QueryBuilders.multiMatch()
                        .fields("name") // 검색할 필드들
                        .query(keyword)
                        .build())
                .build();

        SearchResponse<ProjectDocumentReq> response = openSearchClient.search(searchRequest, ProjectDocumentReq.class);

        return response.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }


}
