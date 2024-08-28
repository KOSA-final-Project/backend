package site.hesil.latteve_spring.domains.search.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.hesil.latteve_spring.domains.search.dto.member.request.MemberDocumentReq;
import site.hesil.latteve_spring.domains.search.dto.project.request.ProjectDocumentReq;
import site.hesil.latteve_spring.domains.search.service.OpenSearchService;
import site.hesil.latteve_spring.domains.search.service.SearchService;

import java.io.IOException;
import java.util.*;


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
@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final OpenSearchService openSearchService;
    private final SearchService searchService;

    @GetMapping("/test-opensearch")
    public String testOpenSearchConnection() {
        log.info("controller : test opensearch connection");
        return openSearchService.checkConnection();

    }

    @GetMapping("/members")
    public List<MemberDocumentReq> searchMembers(@RequestParam String keyword) throws IOException {
        return searchService.searchMembersByKeyword(keyword);
    }

    @GetMapping("/projects")
    public List<ProjectDocumentReq> searchProjects(@RequestParam String keyword) throws IOException {
        return searchService.searchProjectsByKeyword(keyword);
    }

    @GetMapping("/all")
    public Map<String, Object> searchAll(@RequestParam String keyword) throws IOException {
        return searchService.searchAllByKeyword(keyword);
    }

}
