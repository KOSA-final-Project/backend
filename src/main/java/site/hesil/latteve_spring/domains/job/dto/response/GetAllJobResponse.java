package site.hesil.latteve_spring.domains.job.dto.response;

import lombok.Builder;
import site.hesil.latteve_spring.domains.job.domain.Job;

/**
 * packageName    : site.hesil.latteve_spring.domains.job.dto.resposne
 * fileName       : GetAllJobResponse
 * author         : Yeong-Huns
 * date           : 2024-09-02
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-02        Yeong-Huns       최초 생성
 */
@Builder
public record GetAllJobResponse (
        long jobId,
        String name
){
    public static GetAllJobResponse from (Job job){
        return GetAllJobResponse.builder()
                .jobId(job.getJobId())
                .name(job.getName())
                .build();
    }
}
