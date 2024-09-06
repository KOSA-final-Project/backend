package site.hesil.latteve_spring.domains.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.hesil.latteve_spring.domains.member.controller.MemberController;
import site.hesil.latteve_spring.domains.project.domain.Project;
import site.hesil.latteve_spring.domains.project.dto.project.request.ProjectApplyRequest;
import lombok.extern.log4j.Log4j2;
import site.hesil.latteve_spring.domains.project.dto.project.response.ProjectCardResponse;
import site.hesil.latteve_spring.domains.project.dto.project.response.ProjectDetailResponse;
import site.hesil.latteve_spring.domains.project.dto.request.projectSave.ProjectSaveRequest;
import site.hesil.latteve_spring.domains.project.dto.response.ApplicationResponse;
import site.hesil.latteve_spring.domains.project.service.ProjectService;

import site.hesil.latteve_spring.global.security.annotation.AuthMemberId;

import java.util.List;

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
 */
@Log4j2
@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final MemberController memberController;

    // 프로젝트 상세 정보 조회
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDetailResponse> getProjectDetail(@PathVariable Long projectId) {

        ProjectDetailResponse projectDetailResponse = projectService.getProjectDetail(projectId);

        return ResponseEntity.ok(projectDetailResponse);
    }

    // 프로젝트 지원
    @PostMapping("/{projectId}/applications")
    public ResponseEntity<Void> applyProject(@PathVariable Long projectId, @RequestBody ProjectApplyRequest projectApplyRequest) {

        projectService.applyProject(projectId, projectApplyRequest.memberId(), projectApplyRequest.jobId());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping
    public ResponseEntity<Void> projectSave(@RequestBody ProjectSaveRequest projectSaveRequest, @AuthMemberId Long memberId) {
        log.info("projectSaveRequest: {}", projectSaveRequest);
        log.info("memberId: {}", memberId);
        projectService.saveProject(projectSaveRequest, memberId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{projectId}/applications")
    public ResponseEntity<List<ApplicationResponse>> projectRecruit(@PathVariable Long projectId) {
        return ResponseEntity.ok(projectService.getApplicationsByProjectId(projectId));
    }

    // 마이페이지에서 프로젝트 조회
    @GetMapping("/my")
    public ResponseEntity<Page<ProjectCardResponse>> getProjectList(@AuthMemberId Long memberId, Integer status,
                                                                    @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "4") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProjectCardResponse> projectCardResponsePage =  projectService.getProjectsByMemberAndStatus(memberId, status, pageable);
        return ResponseEntity.ok(projectCardResponsePage);
    }

    // 사용자가 '좋아요'한 프로젝트 조회
    @GetMapping("/my/like")
    public ResponseEntity<Page<ProjectCardResponse>> getProjectListByMemberAndLike(@AuthMemberId Long memberId,
                                                                                   @RequestParam(defaultValue = "0") int page,
                                                                                   @RequestParam(defaultValue = "4") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProjectCardResponse> projectPage = projectService.getProjectsByMemberAndLike(memberId, pageable);
        return ResponseEntity.ok(projectPage);
    }

    // top 100  프로젝트 조회


    //최근에 생성된 프로젝트 조회
    // 신규 프로젝트
    @GetMapping("/new")
    public ResponseEntity<Page<ProjectCardResponse>>  getProjectsByNewest(@RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(defaultValue = "4") int size) {
        Page<ProjectCardResponse> projectPage = projectService.getProjectsByCreatedAt(PageRequest.of(page, size));
        return ResponseEntity.ok(projectPage);
    }




}
