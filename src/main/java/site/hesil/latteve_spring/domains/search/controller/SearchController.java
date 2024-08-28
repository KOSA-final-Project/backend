package site.hesil.latteve_spring.domains.search.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import site.hesil.latteve_spring.domains.search.service.OpenSearchService;


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


@Slf4j
@RestController("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final OpenSearchService openSearchService;

    @GetMapping("/test-opensearch")
    public String testOpenSearchConnection() {
        log.info("controller : test opensearch connection");
        return openSearchService.checkConnection();

    }

   
}
