package site.hesil.latteve_spring.domains.search.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch.core.InfoResponse;
import org.springframework.stereotype.Service;

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
public class OpenSearchService {
    private final OpenSearchClient openSearchClient;

    public String checkConnection() {
        try {
            log.info("service : test opensearch connection");
            InfoResponse response = openSearchClient.info();
            return response.clusterName() + " is up and running!";
        } catch (Exception e) {
            return "Failed to connect to OpenSearch: " + e.getMessage();
        }
    }
}
