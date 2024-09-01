package site.hesil.latteve_spring.domains.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.hesil.latteve_spring.domains.project.dto.project.request.ProjectApplyRequest;
import site.hesil.latteve_spring.domains.project.dto.project.response.ProjectDetailResponse;
import site.hesil.latteve_spring.domains.project.service.ProjectService;

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
 */

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDetailResponse> getProjectDetail(@PathVariable Long projectId) {

        ProjectDetailResponse projectDetailResponse = projectService.getProjectDetail(projectId);

        return ResponseEntity.ok(projectDetailResponse);
    }

    @PostMapping("/{projectId}/applications")
    public ResponseEntity<Void> applyProject(@PathVariable Long projectId, @RequestBody ProjectApplyRequest projectApplyRequest) {

        projectService.applyProject(projectId, projectApplyRequest.memberId(), projectApplyRequest.jobId());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
