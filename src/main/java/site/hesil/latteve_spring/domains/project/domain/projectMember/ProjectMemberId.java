package site.hesil.latteve_spring.domains.project.domain.projectMember;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * packageName    : site.hesil.latteve_spring.domains.projectMember.domain
 * fileName       : ProjectMemberId
 * author         : JooYoon
 * date           : 2024-08-26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-26        JooYoon       최초 생성
 */

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ProjectMemberId implements Serializable {

    private long projectId;
    private long memberId;
    private long jobId;
}
