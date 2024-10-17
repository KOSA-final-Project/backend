package site.hesil.latteve_spring.domains.project.controller;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.hesil.latteve_spring.domains.project.dto.project.response.ProjectCardResponse;
import site.hesil.latteve_spring.domains.project.dto.request.projectSave.ProjectSaveRequest;
import site.hesil.latteve_spring.domains.project.service.ProjectService;
import site.hesil.latteve_spring.global.security.annotation.AuthMemberId;
import site.hesil.latteve_spring.global.security.annotation.LoginFilterMemberId;

/**
 * packageName    : site.hesil.latteve_spring.domains.project.controller
 * fileName       : ProjectController
 * author         : JooYoon
 * date           : 2024-08-26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-26        JooYoon       최초 생성
 * 2024-09-01        Yeong-Huns    프로젝트 생성
 * 2024-09-04        Heeseon       프로젝트 상태, 멤버별로 조회
 * 2024-09-07        Yeong-Huns    프로젝트 지원자 승인 / 거절
 * 2024-09-08        Yeong-Huns    좋아요, 좋아요 취소
 */
@Log4j2
@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
//    private final MemberController memberController;
//
//    // 프로젝트 상세 정보 조회
//    @GetMapping("/{projectId}")
//    public ResponseEntity<ProjectDetailResponse> getProjectDetail(@PathVariable Long projectId) {
//
//        ProjectDetailResponse projectDetailResponse = projectService.getProjectDetail(projectId);
//
//        return ResponseEntity.ok(projectDetailResponse);
//    }
//
//    // 프로젝트 지원
//    @PostMapping("/{projectId}/applications")
//    public ResponseEntity<Void> applyProject(@PathVariable Long projectId, @AuthMemberId Long memberId, @RequestBody ProjectApplyRequest projectApplyRequest) {
//
//        projectService.applyProject(projectId, memberId, projectApplyRequest.jobId());
//
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }
//
    @PostMapping
    public ResponseEntity<Long> projectSave(@RequestBody ProjectSaveRequest projectSaveRequest, Long memberId) { // 추후 @AuthMembers 로 변경
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.saveProject(projectSaveRequest, memberId));
    }
//
//    @GetMapping("/{projectId}/applications")
//    public ResponseEntity<List<ApplicationResponse>> projectRecruit(@PathVariable Long projectId) {
//        return ResponseEntity.ok(projectService.getApplicationsByProjectId(projectId));
//    }
//
//    @PutMapping("/applications")
//    public ResponseEntity<Void> updateAcceptStatus(@RequestBody UpdateAcceptStatusRequest updateAcceptStatusRequest, @AuthMemberId Long memberId){
//        projectService.updateAcceptStatus(updateAcceptStatusRequest, memberId);
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//    }
//
//    @PostMapping("/start")
//    public ResponseEntity<Void> projectStart(@RequestBody ProjectStartRequest projectStartRequest, @AuthMemberId Long memberId) {
//        log.info("projectStart: {}", projectStartRequest.projectId());
//        projectService.projectStart(projectStartRequest.projectId(), memberId);
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//    }
//
//    // 마이페이지에서 프로젝트 조회
//    @GetMapping("/my")
//    public ResponseEntity<Page<ProjectCardResponse>> getProjectList(@AuthMemberId Long memberId, Integer status,
//                                                                    @RequestParam(defaultValue = "0") int page,
//                                                                    @RequestParam(defaultValue = "4") int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        Page<ProjectCardResponse> projectCardResponsePage = projectService.getProjectsByMemberAndStatus(memberId, status, pageable);
//        return ResponseEntity.ok(projectCardResponsePage);
//    }
//
//    // 사용자가 '좋아요'한 프로젝트 조회
//    @GetMapping("/my/like")
//    public ResponseEntity<Page<ProjectCardResponse>> getProjectListByMemberAndLike(@AuthMemberId Long memberId,
//                                                                                   @RequestParam(defaultValue = "0") int page,
//                                                                                   @RequestParam(defaultValue = "4") int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        Page<ProjectCardResponse> projectPage = projectService.getProjectsByMemberAndLike(memberId, pageable);
//        return ResponseEntity.ok(projectPage);
//    }
//
    //최근에 생성된 프로젝트 조회
    @GetMapping("/new")
    public ResponseEntity<Page<ProjectCardResponse>>  getProjectsByNewest(
             @LoginFilterMemberId(required = false) @Parameter(required = false) Long memberId,
                                                                          @RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(defaultValue = "4") int size) {
        Page<ProjectCardResponse> projectPage = projectService.getNewProjects(PageRequest.of(page, size),memberId);
        return ResponseEntity.ok(projectPage);
    }

//    //최근에 종료된 프로젝트 조회
//    @GetMapping("/done")
//    public ResponseEntity<Page<ProjectCardResponse>>  getProjectsRecentlyDone(@LoginFilterMemberId(required = false) Long memberId,
//                                                                              @RequestParam(defaultValue = "0") int page,
//                                                                              @RequestParam(defaultValue = "4") int size) {
//
//        Page<ProjectCardResponse> projectPage = projectService.getProjectsByDeadline(PageRequest.of(page, size),memberId);
//        return ResponseEntity.ok(projectPage);
//    }
//
//    // 회고 조회
//    @GetMapping("/{projectId}/retrospectives")
//    public ResponseEntity<RetrospectiveResponse> getRetrospective(@PathVariable Long projectId,
//                                                                  @RequestParam Long memberId,
//                                                                  @RequestParam int week) {
//
//        return ResponseEntity.ok(projectService.getRetrospective(projectId, memberId, week));
//    }
//
//    // 프로젝트 회고 등록
//    @PostMapping("/{projectId}/retrospectives")
//    public ResponseEntity<Void> saveRetrospective(@PathVariable Long projectId,
//                                                  @AuthMemberId Long memberId,
//                                                  @RequestBody CreateRetrospectiveRequest createRetrospectiveRequest) {
//
//        projectService.saveRetrospective(projectId, memberId, createRetrospectiveRequest);
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }
//
//    // 프로젝트 회고 수정
//    @PutMapping("/{projectId}/retrospectives/{retrospectiveId}")
//    public ResponseEntity<Void> updateRetrospective(@PathVariable Long projectId,
//                                                    @PathVariable Long retrospectiveId,
//                                                    @RequestBody UpdateRetrospectiveRequest updateRetrospectiveRequest) {
//
//        projectService.updateRetrospective(retrospectiveId, updateRetrospectiveRequest);
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//    }
//
//    @PostMapping("/{projectId}/like")
//    public ResponseEntity<Void> registerProjectLike(@PathVariable long projectId, @AuthMemberId Long memberId) {
//        projectService.registerProjectLike(projectId,memberId);
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//    }
//
//    @DeleteMapping("/{projectId}/like")
//    public ResponseEntity<Void> deleteProjectLike(@PathVariable long projectId, @AuthMemberId Long memberId) {
//        projectService.deleteProjectLike(projectId,memberId);
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//    }
//
//    @GetMapping("/popular")
//    public ResponseEntity<List<PopularProjectResponse>>  getPopular() {
//
//        List<PopularProjectResponse> projectList = projectService.getTop10PopularProjects();
//        return ResponseEntity.ok(projectList);
//    }
//
//    // 프로젝트 지원 여부 확인
//    @GetMapping("/{projectId}/applications/status")
//    public ResponseEntity<Map<String, Boolean>> isApplication(@PathVariable Long projectId, @AuthMemberId Long memberId ) {
//
//        boolean hasApplied = projectService.isApplication(projectId, memberId);
//
//        Map<String, Boolean> response = Map.of("hasApplied", hasApplied);
//
//        return ResponseEntity.ok(response);
//    }
//
//    // 프로젝트 참여 여부 확인
//    @GetMapping("/{projectId}/participation")
//    public ResponseEntity<Map<String, Boolean>> isParticipation(@PathVariable Long projectId, @AuthMemberId Long authMemberId, @RequestParam Long selectedMemberId) {
//
//        Map<String, Boolean> response = Map.of("isParticipating", authMemberId.equals(selectedMemberId));
//
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping("/isLeader")
//    public ResponseEntity<Boolean> isLeader(@RequestBody IsLeaderRequest isLeaderRequest, @AuthMemberId Long memberId) {
//        return ResponseEntity.ok(isLeaderRequest.memberId() == memberId);
//    }
}
