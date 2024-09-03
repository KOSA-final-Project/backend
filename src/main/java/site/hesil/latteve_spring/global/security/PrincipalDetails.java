package site.hesil.latteve_spring.global.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import site.hesil.latteve_spring.domains.member.domain.Member;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * packageName    : site.hesil.latteve_spring.global.security
 * fileName       : PrincipalDetails
 * author         : yunbin
 * date           : 2024-08-23
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-23           yunbin           최초 생성
 */
public record PrincipalDetails(
        Member member,
        Map<String, Object> attributes,
        String attributeKey,
        String provider) implements OAuth2User, UserDetails {

    @Override
    public String getName() {
        return attributes.get(attributeKey).toString();
    } // providerId

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() { return member.getEmail(); }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}