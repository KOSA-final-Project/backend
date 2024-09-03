package site.hesil.latteve_spring.global.security.jwt;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

/**
 * packageName    : site.hesil.latteve_spring.global.security
 * fileName       : Token
 * author         : yunbin
 * date           : 2024-08-29
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-29           yunbin           최초 생성
 */
@Getter
@AllArgsConstructor
@RedisHash(value = "jwt", timeToLive = 60 * 60 * 24)
public class Token {

    @Id
    private String id;

    private String refreshToken;

    @Indexed
    private String accessToken;

    public Token updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public void updateAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}