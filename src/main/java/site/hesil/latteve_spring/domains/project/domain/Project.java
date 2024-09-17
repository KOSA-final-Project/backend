package site.hesil.latteve_spring.domains.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import site.hesil.latteve_spring.domains.project.listener.ProjectListener;
import site.hesil.latteve_spring.global.audit.entity.BaseTimeEntity;

import java.time.LocalDateTime;

/**
 * packageName    : site.hesil.latteve_spring.domains.project.domain
 * fileName       : Project
 * author         : Yeong-Huns
 * date           : 2024-08-26
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-26        Yeong-Huns       최초 생성
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(ProjectListener.class)
@SQLDelete(sql = "UPDATE project SET deleted_at = NOW() where project_id = ?")
public class Project extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    private String name;
    private String description;
    private String imgUrl;
    private int status;

    private int duration;
    private int cycle;
    private LocalDateTime deletedAt;
    private LocalDateTime startedAt;

    @Builder
    public Project(String name, String description, String imgUrl, int status, int duration, int cycle) {
        this.name = name;
        this.description = description;
        this.imgUrl = imgUrl;
        this.status = status;
        this.duration = duration;
        this.cycle = cycle;
    }
    @JsonIgnore
    // 마감일을 계산하는 메서드
    public LocalDateTime getDeadline() {
        if (this.startedAt != null && this.duration > 0) {
            return this.startedAt.plusDays(this.duration * 7);
        }
        return null;
    }

    public void onGoing(){
        this.status = 1;
    }
}
