package site.hesil.latteve_spring.domains.job.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hesil.latteve_spring.domains.job.domain.Job;

/**
 *packageName    : site.hesil.latteve_spring.domains.job.repository
 * fileName       : JobRepository
 * author         : Heeseon
 * date           : 2024-08-31
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-31        Heeseon       최초 생성
 */
public interface JobRepository extends JpaRepository<Job, Long> {
}
