package site.hesil.latteve_spring.domains.job.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.hesil.latteve_spring.domains.job.dto.response.GetAllJobResponse;
import site.hesil.latteve_spring.domains.job.repository.JobRepository;

import java.util.List;

/**
 * packageName    : site.hesil.latteve_spring.domains.job.service
 * fileName       : JobService
 * author         : yunbin
 * date           : 2024-09-02
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-02           yunbin           최초 생성
 */
@RequiredArgsConstructor
@Service
public class JobService {
    private final JobRepository jobRepository;

    public List<GetAllJobResponse> getAllJobs() {
        return jobRepository.findAll().stream()
                .map(GetAllJobResponse::from)
                .toList();
    }
}
