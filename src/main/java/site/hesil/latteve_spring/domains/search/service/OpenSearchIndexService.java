package site.hesil.latteve_spring.domains.search.service;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.Index;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.mapping.Property;
import org.opensearch.client.opensearch._types.mapping.TypeMapping;
import org.opensearch.client.opensearch.core.InfoResponse;
import org.opensearch.client.opensearch.indices.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * packageName    : site.hesil.latteve_spring.domains.search.service
 * fileName       : OpenSearchService
 * author         : Heeseon
 * date           : 2024-08-27
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-27        Heeseon       최초 생성
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OpenSearchIndexService {
    private final OpenSearchClient openSearchClient;
    private final SearchIndexingService searchIndexingService;

    public String checkConnection() {
        try {
            log.info("service : test opensearch connection");
            InfoResponse response = openSearchClient.info();
            return response.clusterName() + " is up and running!";
        } catch (Exception e) {
            return "Failed to connect to OpenSearch: " + e.getMessage();
        }
    }

    // 나중에 삭제 -> 데이터 동기화
//    @PostConstruct
//    public void init() throws IOException {
//        createOrRecreateIndexWithMapping("projects"); // projects 인덱스 생성 또는 재생성
//        createOrRecreateIndexWithMapping("members");  // members 인덱스 생성 또는 재생성
//        searchIndexingService.indexProjectsToOpenSearch();  // 프로젝트 데이터 인덱싱
//        searchIndexingService.indexMembersToOpenSearch(); // 멤버 데이터 인덱싱
//
//    }
    private void createOrRecreateIndexWithMapping(String indexName) throws IOException {
        // 인덱스 존재 여부 확인
        boolean exists = openSearchClient.indices().exists(e -> e.index(indexName)).value();

        if (exists) {
            // 인덱스가 존재할 경우 삭제
            openSearchClient.indices().delete(new DeleteIndexRequest.Builder().index(indexName).build());
        }

        // 인덱스별 techStack 매핑 설정
        TypeMapping mapping = createMapping(indexName);

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
    private TypeMapping createMapping(String indexName) {
        String filedName;
        Property techStackProperty = Property.of(b -> b
                .nested(o -> o
                        .properties("key", Property.of(pb -> pb.text(t -> t)))
                        .properties("value", Property.of(pb -> pb.text(t -> t)))
                )
        );
        Map<String, Property> properties = new HashMap<>();
        if("projects".equals(indexName)) {
            properties.put("projectTechStack", techStackProperty);
            // project의 name 필드에 keyword 서브 필드 추가
            properties.put("name", createTextFieldWithKeyword());
        }else if("members".equals(indexName)){
            properties.put("techStack", techStackProperty);
            // memberNickname 필드에 keyword 서브 필드 추가
            properties.put("memberNickname", createTextFieldWithKeyword());
        }else {
            throw new IllegalArgumentException("Unknown index name: " + indexName);
        }
        // 매핑 생성
        return TypeMapping.of(mb -> mb
                .properties(properties)
        );
    }

    // text 타입인 필드에 keyword 서브 필드 추가
    private Property createTextFieldWithKeyword(){
        return Property.of(b-> b.text(t -> t.fields("keyword", Property.of(pb->pb.keyword(k -> k )))));
    }

}
