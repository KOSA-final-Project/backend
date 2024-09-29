package site.hesil.latteve_spring.domains.techStack.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.hesil.latteve_spring.domains.techStack.dto.response.GetAllTechStackResponse;
import site.hesil.latteve_spring.domains.techStack.service.TechStackService;

import java.util.List;

/**
 * packageName    : site.hesil.latteve_spring.domains.techStack.controller
 * fileName       : TechStackController
 * author         : Yeong-Huns
 * date           : 2024-09-01
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-01        Yeong-Huns       최초 생성
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/tech-stacks")
public class TechStackController {
//    private final TechStackService techStackService;
//
//    @GetMapping
//    public ResponseEntity<List<GetAllTechStackResponse>> getAllTechStacks(){
//        return ResponseEntity.ok(techStackService.getAllTechStack());
//    }
}
