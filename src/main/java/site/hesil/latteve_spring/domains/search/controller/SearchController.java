package site.hesil.latteve_spring.domains.search.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.hesil.latteve_spring.domains.project.dto.project.response.ProjectCardResponse;
import site.hesil.latteve_spring.domains.search.dto.member.request.MemberDocumentReq;
import site.hesil.latteve_spring.domains.search.dto.project.request.ProjectDocumentReq;
import site.hesil.latteve_spring.domains.search.service.OpenSearchIndexService;
import site.hesil.latteve_spring.domains.search.service.SearchService;
import site.hesil.latteve_spring.global.error.errorcode.ErrorCode;
import site.hesil.latteve_spring.global.error.exception.CustomBaseException;
import site.hesil.latteve_spring.global.security.annotation.AuthMemberId;
import site.hesil.latteve_spring.global.security.annotation.LoginFilterMemberId;

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
    private final OpenSearchIndexService openSearchService;
    private final SearchService searchService;

    @GetMapping("/test-opensearch")
    public String testOpenSearchConnection() {
        log.info("controller : test opensearch connection");
        return openSearchService.checkConnection();
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberDocumentReq>> searchMembers(@RequestParam(required=false) String keyword,
                                                 @RequestParam(required = false) String sortby,
                                                                 @RequestParam(defaultValue = "50") int size, // 기본값 50으로 설정
                                                                 @RequestParam(defaultValue = "0") int from) throws IOException {

        return ResponseEntity.ok(searchService.searchMembersByKeyword(keyword, sortby, from, size ));
    }

    @GetMapping("/projects")
    public ResponseEntity<List<ProjectCardResponse>> searchProjects(@LoginFilterMemberId(required = false) Long memberId,
                                                                    @RequestParam(required = false) String keyword,
                                                                   @RequestParam(required = false) String status,
                                                                   @RequestParam(required = false) String sortby,
                                                                    @RequestParam(defaultValue = "50") int size, // 기본값 50으로 설정
                                                                    @RequestParam(defaultValue = "0") int from) throws IOException {
        log.info("keyword = " + keyword);

        return ResponseEntity.ok(searchService.searchProjectsByKeyword(memberId, keyword,status, sortby, from, size));
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> search(@LoginFilterMemberId(required = false) Long memberId,
                                                      @RequestParam String keyword,@RequestParam(defaultValue = "50") int size, // 기본값 50으로 설정
                                                      @RequestParam(defaultValue = "0") int from) throws IOException {
        List<ProjectCardResponse> projects = searchService.searchProjectsByKeyword(memberId, keyword,null, null, from, size);
        List<MemberDocumentReq> members = searchService.searchMembersByKeyword(keyword, null , from , size);

        Map<String, Object> response = new HashMap<>();
        response.put("projects", projects);
        response.put("members", members);

        return ResponseEntity.ok(response);
    }



}
