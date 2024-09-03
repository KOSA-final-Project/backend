package site.hesil.latteve_spring.domains.project.controller;

import com.amazonaws.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.hesil.latteve_spring.domains.project.domain.Project;
import site.hesil.latteve_spring.domains.project.dto.project.response.ProjectDetailResponse;
import site.hesil.latteve_spring.domains.project.dto.request.projectSave.ProjectSaveRequest;
import site.hesil.latteve_spring.domains.project.dto.response.ApplicationResponse;
import site.hesil.latteve_spring.domains.project.service.ProjectService;

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
 */
@Log4j2
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

    @PostMapping("")
    public ResponseEntity<Void> projectSave(@RequestBody ProjectSaveRequest projectSaveRequest) {
        log.info("projectSaveRequest: {}", projectSaveRequest);
        projectService.saveProject(projectSaveRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{projectId}/applications")
    public ResponseEntity<List<ApplicationResponse>> projectRecruit(@PathVariable Long projectId) {
        return ResponseEntity.ok(projectService.getApplicationsByProjectId(projectId));
    }
}
