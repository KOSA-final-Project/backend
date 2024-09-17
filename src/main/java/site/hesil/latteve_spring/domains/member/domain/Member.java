package site.hesil.latteve_spring.domains.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.hesil.latteve_spring.domains.member.listener.MemberListener;
import site.hesil.latteve_spring.domains.memberStack.domain.MemberStack;
import site.hesil.latteve_spring.global.audit.entity.BaseTimeEntity;

import java.time.LocalDateTime;
import java.util.List;

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
 * 2024-08-28        yunbin        생성자 수정, updateProfile() 추가
 * 2024-08-29        yunbin        provider, provideId 컬럼 추가
 */

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(MemberListener.class)
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
    private String provider; //어떤 OAuth인지(google, naver 등)
    private String providerId; // 해당 OAuth 의 key(id)

    @Builder
    public Member(String email, String nickname, String github, String imgUrl, String career, String pr, LocalDateTime deletedAt, String provider, String providerId) {
        this.email = email;
        this.nickname = nickname != null ? nickname : "";
        this.github = github;
        this.imgUrl = imgUrl;
        this.career = career != null ? career : "";
        this.pr = pr;
        this.deletedAt = deletedAt;
        this.provider = provider;
        this.providerId = providerId;
    }

    public void updateProfile(String nickname, String career, String github) {
        if (nickname != null) {
            this.nickname = nickname;
        }
        if (career != null) {
            this.career = career;
        }
        if (github != null) {
            this.github = github;
        }
    }

    public void updateMemberInfo(String imgUrl, String nickname, String career, String github,  String pr){
        if(imgUrl != null){
            this.imgUrl = imgUrl;
        }
        if(nickname != null){
            this.nickname = nickname;
        }
        if(career != null){
            this.career = career;
        }
        if(github != null){
            this.github = github;
        }
        if(pr != null){
            this.pr = pr;
        }
    }

    public void updateProvider(String provider, String providerId) {
        if (provider != null) {
            this.provider = provider;
        }
        if (providerId != null) {
            this.providerId = providerId;
        }
    }
}
