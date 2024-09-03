package site.hesil.latteve_spring.domains.job.dto.response;

import site.hesil.latteve_spring.domains.job.domain.Job;

/**
 * packageName    : site.hesil.latteve_spring.domains.job.dto.response
 * fileName       : ResponseJob
 * author         : yunbin
 * date           : 2024-09-02
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-02           yunbin           최초 생성
 */
public record ResponseJob(long jobId, String name) {
    public static ResponseJob of(Job job) {
        return new ResponseJob(job.getJobId(), job.getName());
    }
}
