package site.hesil.latteve_spring.domains.job.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.hesil.latteve_spring.domains.job.dto.response.ResponseJob;
import site.hesil.latteve_spring.domains.job.service.JobService;

import java.util.List;

/**
 * packageName    : site.hesil.latteve_spring.domains.job.controller
 * fileName       : JobController
 * author         : yunbin
 * date           : 2024-09-02
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-02           yunbin           최초 생성
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jobs")
public class JobController {
    private final JobService jobService;

    @GetMapping
    public ResponseEntity<List<ResponseJob>> getAllJobs() {
        log.info("getAllJobs 호출됨");
        List<ResponseJob> jobs = jobService.getAllJobs();
        return ResponseEntity.ok(jobs);
    }
}
