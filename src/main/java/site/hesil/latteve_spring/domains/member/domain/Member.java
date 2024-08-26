package site.hesil.latteve_spring.domains.member.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.hesil.latteve_spring.global.audit.entity.BaseTimeEntity;

import java.time.LocalDateTime;

/**
 * packageName    : site.hesil.latteve_spring.domains.member.domain
 * fileName       : Member
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
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;
    private String email;
    private String nickname;
    private String github;
    private String imgUrl;
    private String career;
    private String pr;
    private LocalDateTime deletedAt;

    @Builder
    public Member(String email, String imgUrl) {
        this.email = email;
        this.imgUrl = imgUrl;
    }
}
