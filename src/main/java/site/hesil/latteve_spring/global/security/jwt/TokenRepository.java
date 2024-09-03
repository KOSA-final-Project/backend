package site.hesil.latteve_spring.global.security.jwt;

/**
 * packageName    : site.hesil.latteve_spring.global.security
 * fileName       : TokenRepository
 * author         : yunbin
 * date           : 2024-08-29
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-29           yunbin           최초 생성
 */

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends CrudRepository<Token, String> {

    Optional<Token> findByAccessToken(String accessToken);
}