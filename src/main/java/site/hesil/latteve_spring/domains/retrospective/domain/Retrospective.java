package site.hesil.latteve_spring.domains.retrospective.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.hesil.latteve_spring.domains.project.domain.recruitment.Recruitment;
import site.hesil.latteve_spring.global.audit.entity.BaseTimeEntity;

/**
 * packageName    : site.hesil.latteve_spring.domains.retrospective.domain
 * fileName       : Retrospective
 * author         : JooYoon
 * date           : 2024-08-26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-26        JooYoon       최초 생성
 */

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Retrospective extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long retId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_id")
    private Recruitment recruitment;

    private String title;
    private String content;
    private int week;
    private String isDeleted;

    @Builder
    private Retrospective(String title, String content, int week) {
        this.title = title;
        this.content = content;
        this.week = week;
        this.isDeleted = "n";
    }

    public static Retrospective of(String title, String content, int week) {
        return new Retrospective(title, content, week);
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
