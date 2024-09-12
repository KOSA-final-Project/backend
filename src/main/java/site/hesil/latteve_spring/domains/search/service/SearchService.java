package site.hesil.latteve_spring.domains.search.service;

import lombok.extern.slf4j.Slf4j;
import org.opensearch.client.opensearch._types.FieldValue;
import org.opensearch.client.opensearch._types.SortOptions;
import org.opensearch.client.opensearch._types.SortOrder;
import org.opensearch.client.opensearch._types.query_dsl.*;
import lombok.RequiredArgsConstructor;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch.core.SearchRequest;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.opensearch.client.opensearch.core.search.Hit;
import org.springframework.stereotype.Service;
import site.hesil.latteve_spring.domains.project.repository.projectLike.ProjectLikeRepository;
import site.hesil.latteve_spring.domains.search.dto.member.request.MemberDocumentReq;
import site.hesil.latteve_spring.domains.project.dto.project.response.ProjectCardResponse;
import site.hesil.latteve_spring.domains.search.dto.project.request.ProjectDocumentReq;

import java.io.IOException;
import java.util.*;
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
@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {

    private final OpenSearchClient openSearchClient;
    private final ProjectLikeRepository projectLikeRepository;

    /** project search */
    public List<ProjectCardResponse> searchProjectsByKeyword(Long memberId, String keyword, String status, String sort, int from, int size) throws IOException {

        // keyword로 검색
        BoolQuery boolQuery = BoolQuery.of(b -> {
            if (keyword != null && !keyword.trim().isEmpty()) {
                // 프로젝트 이름으로 검색
                b.should(MatchQuery.of(m -> m.field("name").query(FieldValue.of(keyword)).fuzziness("AUTO"))._toQuery());
                // 기술 스택 이름으로 검색
                b.should(NestedQuery.of(n -> n
                        .path("projectTechStack")
                        .query(q -> q.match(m -> m
                                .field("projectTechStack.name")
                                .query(FieldValue.of(keyword))
                                .fuzziness("AUTO")
                        ))
                )._toQuery());
                // 최소 일치 조건: 하나 이상의 필드가 일치해야 함
                b.minimumShouldMatch(String.valueOf(1));
            } else {
                // keyword가 없으면 모든 프로젝트 반환
                b.must(MatchAllQuery.of(m -> m)._toQuery());
            }

            // project 상태로 필터링
            if (status != null && !status.isEmpty() && !status.equals("전체")) {
                log.info("search projects - status: " + status);
                b.filter(TermQuery.of(t -> t.field("status").value(FieldValue.of(status)))._toQuery());
            }
            return b;
        });

        // SearchRequest 빌드
        SearchRequest.Builder searchRequest = new SearchRequest.Builder()
                .index("projects")  // 검색할 인덱스 이름
                .query(boolQuery._toQuery())
                .from(from * size)  // 시작 인덱스 (예: 첫 번째 페이지)
                .size(size);  // 페이지 당 size 만큼 반환

        // 정렬 기준 설정
        if (sort != null && !sort.isEmpty()) {
            if ("cntLike".equalsIgnoreCase(sort)) {
                searchRequest.sort(SortOptions.of(s -> s
                        .field(f -> f
                                .field("cntLike")  // 좋아요 수로 정렬
                                .order(SortOrder.Desc)  // 내림차순 (많이 받은 순)
                        )
                ));
            } else if ("latest".equalsIgnoreCase(sort)) {
                searchRequest.sort(SortOptions.of(s -> s
                        .field(f -> f
                                .field("createdAt")  // 생성일자로 정렬
                                .order(SortOrder.Desc)  // 내림차순 (최신순)
                        )
                ));
            }
        }

        // OpenSearch 클라이언트를 사용하여 검색 요청 실행
        SearchResponse<ProjectDocumentReq> response = openSearchClient.search(searchRequest.build(), ProjectDocumentReq.class);

        // 검색된 프로젝트 리스트 가져오기
        List<ProjectDocumentReq> projects = response.hits().hits().stream()
                .map(hit -> hit.source())
                .collect(Collectors.toList());

        // 로그인된 상태일 때, 각 프로젝트의 좋아요 여부를 한 번에 확인
        if (memberId != null) {
            List<Long> projectIds = projects.stream()
                    .map(ProjectDocumentReq::projectId)
                    .collect(Collectors.toList());

            // 사용자가 좋아요한 프로젝트 ID 조회
            Set<Long> likedProjectIds = new HashSet<>(projectLikeRepository.findLikedProjectIdsByMemberIdAndProjectIds(memberId, projectIds));

            // 좋아요 여부를 반영하여 ProjectCardResponse로 변환
            return projects.stream()
                    .map(project -> mapToProjectCardResponse(project, likedProjectIds.contains(project.projectId())))
                    .collect(Collectors.toList());
        }

        // 로그인되지 않은 경우 좋아요 여부 없이 ProjectCardResponse 반환
        return projects.stream()
                .map(project -> mapToProjectCardResponse(project, false))
                .collect(Collectors.toList());

    }
    // ProjectDocumentReq를 ProjectCardResponse로 변환하는 메서드
    public static ProjectCardResponse mapToProjectCardResponse(ProjectDocumentReq project, boolean isLiked) {
        return ProjectCardResponse.builder()
                .projectId(project.projectId())
                .name(project.name())
                .imgUrl(project.imgUrl())
                .duration(project.duration())
                .projectTechStack(project.projectTechStack().stream()
                        .map(tech -> new ProjectCardResponse.TechStack(tech.name(), tech.imgUrl()))
                        .collect(Collectors.toList()))
                .isLiked(isLiked)  // 좋아요 여부 추가
                .cntLike(project.cntLike())  // 좋아요 수
                .currentCnt(project.currentCnt())
                .teamCnt(project.teamCnt())
                .build();
    }


    /**Member search */
    public List<MemberDocumentReq> searchMembersByKeyword(String keyword , String sort, int from , int size) throws IOException {

        BoolQuery boolQuery = BoolQuery.of(b -> {
            if (keyword != null && !keyword.trim().isEmpty()){
                // 라떼버 이름으로 검색
                b.should(MatchQuery.of(m -> m.field("memberNickname").query(FieldValue.of(keyword)).fuzziness("AUTO"))._toQuery());
                // 기술 스택 이름으로 검색
                b.should(NestedQuery.of(n -> n
                        .path("techStack")  // nested 필드의 경로 지정
                        .query(q -> q
                                .match(m -> m
                                        .field("techStack.name")
                                        .query(FieldValue.of(keyword))
                                        .fuzziness("AUTO")
                                )
                        )
                )._toQuery());
                // 경력으로 검색
                b.should(MatchQuery.of(m -> m.field("memberJob").query(FieldValue.of(keyword)).fuzziness("AUTO"))._toQuery());
                // 최소 매칭 조건
                b.minimumShouldMatch(String.valueOf(1));
            }else{
                // keyword가 없으면 모든 라뗴버 반환
                b.must(MatchAllQuery.of(m -> m)._toQuery());
            }

            return b;
        });

        // SearchRequest 빌더에 결합된 쿼리 추가
        SearchRequest.Builder searchRequest = new SearchRequest.Builder()
                .index("members")  // 검색할 인덱스 이름
                .query( boolQuery._toQuery())  // 결합된 쿼리
                .from(from * size)  // 시작 인덱스
                .size(size);  // 페이지 당 size 만큼 반환

        // 정렬 기준 설정
        if (sort != null && !sort.isEmpty()) {
            if ("latest".equalsIgnoreCase(sort)) {
                searchRequest.sort(SortOptions.of(s -> s
                        .field(f -> f
                                .field("createdAt")  // 최신순 정렬
                                .order(SortOrder.Desc)  // 내림차순
                        )
                ));
            } else if ("career_desc".equalsIgnoreCase(sort)) {
                searchRequest.sort(SortOptions.of(s -> s
                        .field(f -> f
                                .field("careerSortValue")  // 경력 기준 정렬
                                .order(SortOrder.Desc)  // 내림차순 (경력 많은 순)
                        )
                ));
            }else if ("career_asc".equalsIgnoreCase(sort)) {
                searchRequest.sort(SortOptions.of(s -> s
                        .field(f -> f
                                .field("careerSortValue")  // 경력 기준 정렬
                                .order(SortOrder.Asc)  //오름차순 (경력 적은 순)
                        )
                ));
            }
        } else {
            // 기본 정렬: 이름순 정렬 (오름차순)
            searchRequest.sort(SortOptions.of(s -> s
                    .field(f -> f
                            .field("memberNickname.keyword")  // 이름순 정렬
                            .order(SortOrder.Asc)  // 오름차순
                    )
            ));
        }

        SearchResponse<MemberDocumentReq> response = openSearchClient.search(searchRequest.build(), MemberDocumentReq.class);

        // 검색 결과를 MemberDocumentReq 리스트로 변환하여 반환
        return response.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());
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