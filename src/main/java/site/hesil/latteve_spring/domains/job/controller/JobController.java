package site.hesil.latteve_spring.domains.job.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.hesil.latteve_spring.domains.job.dto.response.GetAllJobResponse;
import site.hesil.latteve_spring.domains.job.service.JobService;

import java.util.List;

/**
 * packageName    : site.hesil.latteve_spring.domains.job.controller
 * fileName       : JobController
 * author         : Yeong-Huns
 * date           : 2024-09-02
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-02        Yeong-Huns       최초 생성
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/jobs")
public class JobController {
//    private final JobService jobService;
//
//    @GetMapping
//    public ResponseEntity<List<GetAllJobResponse>> getAllJobs() {
//        return ResponseEntity.ok(jobService.getAllJobs());
//    }
}
