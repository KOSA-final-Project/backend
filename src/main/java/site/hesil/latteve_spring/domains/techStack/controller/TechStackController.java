package site.hesil.latteve_spring.domains.techStack.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.hesil.latteve_spring.domains.techStack.dto.response.ResponseTechStack;
import site.hesil.latteve_spring.domains.techStack.service.TechStackService;

import java.util.List;

/**
 * packageName    : site.hesil.latteve_spring.domains.techStack.controller
 * fileName       : TechStackController
 * author         : yunbin
 * date           : 2024-09-01
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-01           yunbin           최초 생성
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tech-stacks")
public class TechStackController {
    private final TechStackService techStackService;

    @GetMapping
    public ResponseEntity<List<ResponseTechStack>> getAllTechStacks() {
        log.info("getAllTechStacks 호출됨");
        List<ResponseTechStack> techStacks = techStackService.getAllTechStacks();
        return ResponseEntity.ok(techStacks);
    }
}
