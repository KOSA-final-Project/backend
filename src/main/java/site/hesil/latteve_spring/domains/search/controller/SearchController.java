package site.hesil.latteve_spring.domains.search.controller;

import lombok.RequiredArgsConstructor;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.HealthStatus;
import org.opensearch.client.opensearch.cluster.HealthResponse;
import org.opensearch.client.opensearch.cluster.OpenSearchClusterClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : site.hesil.latteve_spring.domains.search
 * fileName       : SearchController
 * author         : Heeseon
 * date           : 2024-08-26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-26        Heeseon       최초 생성
 */
@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {
    private final OpenSearchClient openSearchClient;


    @GetMapping("/health")
    public String checkHealth() {
        try {
            // 클러스터 헬스 체크 API 호출
            OpenSearchClusterClient clusterClient = openSearchClient.cluster();
            HealthResponse healthResponse = clusterClient.health();
            HealthStatus status = healthResponse.status();
            return "OpenSearch cluster health status: " + status.toString();
        } catch (Exception e) {
            return "Failed to connect to OpenSearch: " + e.getMessage();
        }
    }
}
