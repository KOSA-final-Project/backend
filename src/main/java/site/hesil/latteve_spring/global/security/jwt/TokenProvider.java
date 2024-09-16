package site.hesil.latteve_spring.global.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import site.hesil.latteve_spring.domains.member.domain.Member;
import site.hesil.latteve_spring.domains.member.repository.MemberRepository;
import site.hesil.latteve_spring.global.error.exception.NotFoundException;
import site.hesil.latteve_spring.global.error.exception.TokenException;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static site.hesil.latteve_spring.global.error.errorcode.ErrorCode.INVALID_JWT_SIGNATURE;
import static site.hesil.latteve_spring.global.error.errorcode.ErrorCode.TOKEN_INVALID;

/**
 * packageName    : site.hesil.latteve_spring.global.security
 * fileName       : TokenProvider
 * author         : yunbin
 * date           : 2024-08-29
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-29           yunbin           최초 생성
 * 2024-09-03           yunbin           토큰에 memberId 추가
 * 2024-09-03           Yeong-Huns       토큰Parser 추가
 * 2024-09-04           yunbin           토큰, 쿠키 만료 시간 수정
 * 2024-09-14           yunbin           배포 서버에서 쿠키 전달 안되는 문제 수정
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class TokenProvider {
    private final MemberRepository memberRepository;
    @Value("${jwt.key}")
    private String key;
    @Value("${jwt.secure-cookie}")
    private boolean secureCookie;
    private SecretKey secretKey;
    //private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30L;
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60L;
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;
    private static final String KEY_ROLE = "role";
    private final TokenService tokenService;

    @PostConstruct
    private void setSecretKey() {
        secretKey = Keys.hmacShaKeyFor(key.getBytes());
    }

    public String generateAccessToken(Authentication authentication) {
        return generateToken(authentication, ACCESS_TOKEN_EXPIRE_TIME); // access token 발급
    }

    // 1. refresh token 발급
    public void generateRefreshToken(Authentication authentication, String accessToken) {
        String refreshToken = generateToken(authentication, REFRESH_TOKEN_EXPIRE_TIME);
        tokenService.saveOrUpdate(authentication.getName(), refreshToken, accessToken); // redis에 저장
    }

    private String generateToken(Authentication authentication, long expireTime) { // 토큰 발급
        Member member = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(NotFoundException::new);

        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + expireTime);

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining());

        return Jwts.builder()
                .subject(authentication.getName())
                .claim(KEY_ROLE, authorities)
                .claim("memberId", member.getMemberId())
                .issuedAt(now)
                .expiration(expiredDate)
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        List<SimpleGrantedAuthority> authorities = getAuthorities(claims);

        // 2. security의 User 객체 생성
        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    private List<SimpleGrantedAuthority> getAuthorities(Claims claims) {
        return Collections.singletonList(new SimpleGrantedAuthority(
                claims.get(KEY_ROLE).toString()));
    }

    // 3. accessToken 재발급
    public String reissueAccessToken(String accessToken) {
        if (StringUtils.hasText(accessToken)) {
            Token token = tokenService.findByAccessTokenOrThrow(accessToken);
            String refreshToken = token.getRefreshToken();
            log.info("Refresh token: {}", refreshToken);

            if (validateToken(refreshToken)) {
                String reissueAccessToken = generateAccessToken(getAuthentication(refreshToken));
                tokenService.updateToken(reissueAccessToken, token);
                log.info("토큰 재발급됨");
                return reissueAccessToken;
            }
        }
        log.info("재발급 실패");
        return null;
    }

    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            log.info("토큰 없음");
            return false;
        }

        Claims claims = parseClaims(token);
        log.info("만료일 {}", claims.getExpiration());
        log.info("현재시간 {}", new Date());
        log.info("{}", claims.getExpiration().after(new Date()));
        return claims.getExpiration().after(new Date());
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build()
                    .parseSignedClaims(token).getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (MalformedJwtException e) {
            throw new TokenException(TOKEN_INVALID);
        } catch (SecurityException e) {
            throw new TokenException(INVALID_JWT_SIGNATURE);
        }
//        try {
//            return Jwts.parser()
//                    .setSigningKey(secretKey)
//                    .parseClaimsJws(token)
//                    .getBody();
//        } catch (ExpiredJwtException e) {
//            return e.getClaims();
//        } catch (MalformedJwtException e) {
//            throw new TokenException(TOKEN_INVALID);
//        } catch (SignatureException e) {
//            throw new TokenException(INVALID_JWT_SIGNATURE);
//        } catch (Exception e) {
//            throw new TokenException("JWT parsing error");
//        }
    }

    public void addJwtCookieToResponse(HttpServletResponse response, String token) {

        String cookieString;

        if (secureCookie) { // 배포 환경 (HTTPS)
            cookieString = String.format(
                    "jwt=%s; SameSite=None; Path=/; Max-Age=%d; Secure",
                    token, 60 * 60
            );
            log.info("배포 환경 쿠키: {}", cookieString);
        } else { // 로컬 환경 (HTTP)
            cookieString = String.format(
                    "jwt=%s; SameSite=Lax; Path=/; Max-Age=%d",
                    token, 60 * 60
            );
            log.info("로컬 환경 쿠키: {}", cookieString);
        }

        response.setHeader("Set-Cookie", cookieString);

        // CORS 설정
        // response.setHeader("Access-Control-Allow-Origin", "https://www.latteve.site");
        // response.setHeader("Access-Control-Allow-Credentials", "true");
    }

    // YH - MemberId 받아오기.
    public Optional<Long> getMemberId(String token) {
        Claims claims = parseClaims(token);
        return Optional.ofNullable(claims.get("memberId", Long.class));
    }

}