package site.hesil.latteve_spring.domains.search.service;

import org.opensearch.client.opensearch._types.FieldValue;
import org.opensearch.client.opensearch.indices.CreateIndexRequest;
import org.opensearch.client.opensearch.indices.CreateIndexResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.mapping.Property;
import org.opensearch.client.opensearch._types.mapping.TypeMapping;
import org.opensearch.client.opensearch._types.query_dsl.Query;
import org.opensearch.client.opensearch._types.query_dsl.QueryBuilders;
import org.opensearch.client.opensearch.core.IndexRequest;
import org.opensearch.client.opensearch.core.SearchRequest;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.opensearch.client.opensearch.core.search.Hit;
import org.opensearch.client.opensearch.indices.DeleteIndexRequest;
import org.springframework.stereotype.Service;
import site.hesil.latteve_spring.domains.member.domain.Member;
import site.hesil.latteve_spring.domains.member.repository.MemberRepository;
import site.hesil.latteve_spring.domains.memberStack.domain.MemberStack;
import site.hesil.latteve_spring.domains.memberStack.repository.MemberStackRepository;
import site.hesil.latteve_spring.domains.project.domain.Project;
import site.hesil.latteve_spring.domains.project.repository.projectmember.ProjectMemberRepository;
import site.hesil.latteve_spring.domains.project.repository.project.ProjectRepository;
import site.hesil.latteve_spring.domains.project.repository.recruitment.RecruitmentRepository;
import site.hesil.latteve_spring.domains.projectStack.domain.ProjectStack;
import site.hesil.latteve_spring.domains.projectStack.repository.ProjectStackRepository;
import site.hesil.latteve_spring.domains.search.dto.member.request.MemberDocumentReq;
import site.hesil.latteve_spring.domains.search.dto.project.request.ProjectDocumentReq;
import site.hesil.latteve_spring.domains.techStack.domain.TechStack;
import site.hesil.latteve_spring.domains.techStack.repository.TechStackRepository;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final MemberStackRepository memberStackRepository;
    private final ProjectRepository projectRepository;
    private final OpenSearchClient openSearchClient;
    private final ProjectStackRepository projectStackRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final RecruitmentRepository recruitmentRepository;
    private final TechStackRepository techStackRepository;

    // 나중에 삭제 -> 데이터 동기화
    @PostConstruct
    public void init() throws IOException {
//        createOrRecreateIndexWithMapping("projects"); // projects 인덱스 생성 또는 재생성
//        createOrRecreateIndexWithMapping("members");  // members 인덱스 생성 또는 재생성
        indexProjectsToOpenSearch();  // 프로젝트 데이터 인덱싱
        indexMembersToOpenSearch(); // 멤버 데이터 인덱싱
    }
    private void createOrRecreateIndexWithMapping(String indexName) throws IOException {
        // 인덱스 존재 여부 확인
        boolean exists = openSearchClient.indices().exists(e -> e.index(indexName)).value();

        if (exists) {
            // 인덱스가 존재할 경우 삭제
            openSearchClient.indices().delete(new DeleteIndexRequest.Builder().index(indexName).build());
        }

        // 인덱스별 매핑 설정
        TypeMapping mapping = createTechStackMapping();

        // 인덱스 생성 요청
        CreateIndexRequest createIndexRequest = new CreateIndexRequest.Builder()
                .index(indexName)
                .mappings(mapping)
                .build();

        // 인덱스 생성
        CreateIndexResponse createIndexResponse = openSearchClient.indices().create(createIndexRequest);

        if (!createIndexResponse.acknowledged()) {
            throw new RuntimeException("Failed to create OpenSearch index with mappings.");
        }
    }

    private TypeMapping createTechStackMapping() {
        Property techStackProperty = Property.of(b -> b
                .object(o -> o
                        .properties("key", Property.of(pb -> pb.text(t -> t)))
                        .properties("value", Property.of(pb -> pb.text(t -> t)))
                )
        );

        return TypeMapping.of(mb -> mb
                .properties("techStack", techStackProperty)
        );
    }


    /** project search */
    @Transactional
    public void indexProjectsToOpenSearch() throws IOException {
        List<Project> projects = projectRepository.findAll();
        //Project -> Index
        for(Project project : projects) {
            // 프로젝트에 연관된 기술 스택  정보 가져옴
            List<ProjectStack> projectTechStacks = projectStackRepository.findAllByProject_ProjectId(project.getProjectId());

            // techStack 이름과 이미지 URL을 Map으로 저장
            Map<String, String> techStackMap = new HashMap<>();
            for (ProjectStack projectStack : projectTechStacks) {
                Optional<TechStack> techStackOpt = techStackRepository.findById(projectStack.getTechStack().getTechStackId());
                if (techStackOpt.isPresent()) {
                    TechStack techStack = techStackOpt.get();
                    String key = techStack.getName();
                    String value = techStack.getImgUrl() != null ? techStack.getImgUrl() :
                            (projectStack.getCustomStack() != null ? projectStack.getCustomStack() : key);
                    techStackMap.put(key, value);
                }
            }

            // 프로젝트에 필요한 인원
            Integer requiredMemberCount = recruitmentRepository.findMemberCountByProject_ProjectId(project.getProjectId());
            // 프로젝트에 지원한 인원
            Integer currentMemberCount = projectMemberRepository.findMemberCountByProject_ProjectId(project.getProjectId());

            // ProjectDocumentReq 생성
            ProjectDocumentReq projectDocumentReq = ProjectDocumentReq.builder()
                    .name(project.getName())
                    .imgUrl(project.getImgUrl())
                    .duration(project.getDuration())
                    .projectTechStack(techStackMap)
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
        // Query 객체를 생성하여 multi_match 쿼리를 구성
        Query query = QueryBuilders.multiMatch()
                .fields("name",  "projectTechStack.key")  // 검색할 필드들
                .query(keyword)  // 검색할 키워드
                .build()._toQuery();

        // SearchRequest 빌더에 쿼리 추가
        SearchRequest searchRequest = new SearchRequest.Builder()
                .index("projects")  // 검색할 인덱스 이름
                .query(query)  // 위에서 구성한 쿼리
                .build();

        // OpenSearch 클라이언트를 사용하여 검색 요청 실행
        SearchResponse<ProjectDocumentReq> response = openSearchClient.search(searchRequest, ProjectDocumentReq.class);

        // 검색 결과를 ProjectDocumentReq 리스트로 변환하여 반환
        return response.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }

    /**Member search */
    @Transactional
    public void indexMembersToOpenSearch() throws IOException {
        List<Member> members = memberRepository.findAll();
        // Member -> Index
        for (Member member : members) {
            // 멤버에 연관된 기술 스택 정보 가져옴
            List<MemberStack> memberStacks = memberStackRepository.findAllByMember_MemberId(member.getMemberId());
            // techStack 이름과 이미지 URL을 Map으로 저장
            Map<String, String> techStackMap = new HashMap<>();
            for (MemberStack memberStack : memberStacks) {
                Optional<TechStack> techStackOpt = techStackRepository.findById(memberStack.getTechStack().getTechStackId());
                if (techStackOpt.isPresent()) {
                    TechStack techStack = techStackOpt.get();
                    String key = techStack.getName();
                    String value = techStack.getImgUrl() != null ? techStack.getImgUrl() : (memberStack.getCustomStack() != null ? memberStack.getCustomStack() : key);
                    techStackMap.put(key, value);
                }
            }
            // MemberDocumentReq 생성
            MemberDocumentReq memberDocumentReq = MemberDocumentReq.builder()
                    .nickname(member.getNickname())
                    .imgUrl(member.getImgUrl())
                    .techStacks(techStackMap)
                    .career(member.getCareer())
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

    public List<MemberDocumentReq> searchMembersByKeyword(String keyword) throws IOException {
        // Query 객체를 생성
        // 여러 필드에 대해 should 절 사용
        Query boolQuery = QueryBuilders.bool()
                .should(QueryBuilders.match().field("nickname").query(FieldValue.of(keyword)).build()._toQuery())
                .should(QueryBuilders.exists().field("techStacks." + keyword).build()._toQuery())
                .should(QueryBuilders.match().field("career").query(FieldValue.of(keyword)).build()._toQuery())
                .minimumShouldMatch(String.valueOf(1))
                .build()._toQuery();

        // SearchRequest 빌더에 결합된 쿼리 추가
        SearchRequest searchRequest = new SearchRequest.Builder()
                .index("members")  // 검색할 인덱스 이름
                .query(boolQuery)  // 결합된 쿼리
                .build();

        SearchResponse<MemberDocumentReq> response = openSearchClient.search(searchRequest, MemberDocumentReq.class);

        // 검색 결과를 MemberDocumentReq 리스트로 변환하여 반환
        return response.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }

    // 통합 검색 메서드
    public Map<String, Object> searchAllByKeyword(String keyword) throws IOException {
        List<MemberDocumentReq> members = searchMembersByKeyword(keyword);
        List<ProjectDocumentReq> projects = searchProjectsByKeyword(keyword);

        Map<String, Object> result = new HashMap<>();
        result.put("members", members);
        result.put("projects", projects);

        return result;
    }

    //member에서 techstack keyword로 검색되는지 확인
    public List<MemberDocumentReq> searchMembersByTechStack(String techStackKey) throws IOException {
        // 특정 키 존재 여부 확인 쿼리 생성
        Query query = createTechStackExistsQuery(techStackKey);

        // SearchRequest 빌더에 결합된 쿼리 추가
        SearchRequest searchRequest = new SearchRequest.Builder()
                .index("members")  // 검색할 인덱스 이름
                .query(query)  // 결합된 쿼리
                .build();

        // OpenSearch 클라이언트를 사용하여 검색 요청 실행
        SearchResponse<MemberDocumentReq> response = openSearchClient.search(searchRequest, MemberDocumentReq.class);

        // 검색 결과를 MemberDocumentReq 리스트로 변환하여 반환
        return response.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }

    // 특정 키 존재 여부 확인 메서드
    public Query createTechStackExistsQuery(String techStackKey) {
        // exists 쿼리 작성
        return QueryBuilders.bool()
                .should(QueryBuilders.exists()
                        .field("techStacks." + techStackKey)
                        .build()._toQuery())
                .build()._toQuery();
    }
}