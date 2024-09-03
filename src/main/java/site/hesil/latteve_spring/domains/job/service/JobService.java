package site.hesil.latteve_spring.domains.job.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.hesil.latteve_spring.domains.job.domain.Job;
import site.hesil.latteve_spring.domains.job.dto.response.ResponseJob;
import site.hesil.latteve_spring.domains.job.repository.JobRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * packageName    : site.hesil.latteve_spring.domains.job.service
 * fileName       : JobService
 * author         : yunbin
 * date           : 2024-09-02
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-02           yunbin           최초 생성
 */
@Service
@RequiredArgsConstructor
public class JobService {
    private final JobRepository jobRepository;

    public List<ResponseJob> getAllJobs() {
        List<Job> jobs = jobRepository.findAll();

        return jobs.stream()
                .map(ResponseJob::of)
                .collect(Collectors.toList());
    }
}
