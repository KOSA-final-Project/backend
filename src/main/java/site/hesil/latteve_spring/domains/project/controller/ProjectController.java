package site.hesil.latteve_spring.domains.project.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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

@RestController("/api/projects")
public class ProjectController {

    @GetMapping("/{projectId}")
    public void getProjectDetail(@PathVariable Long projectId) {
        System.out.println("Hello");
    }
}
