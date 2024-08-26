package site.hesil.latteve_spring.domains.project.domain.projectLike;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * packageName    : site.hesil.latteve_spring.domains.project.domain.projectLike
 * fileName       : ProjectLikeId
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
@EqualsAndHashCode
public class ProjectLikeId implements Serializable {

    private long projectId;
    private long memberId;
}
