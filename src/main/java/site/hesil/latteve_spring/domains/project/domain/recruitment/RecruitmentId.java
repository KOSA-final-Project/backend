package site.hesil.latteve_spring.domains.project.domain.recruitment;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * packageName    : site.hesil.latteve_spring.domains.recruitment.domain
 * fileName       : RecruitmentId
 * author         : JooYoon
 * date           : 2024-08-26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-26        JooYoon       최초 생성
 */

@Getter
@NoArgsConstructor
@Embeddable
@EqualsAndHashCode
public class RecruitmentId implements Serializable {

    private long projectId;
    private long jobId;
}
