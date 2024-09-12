package site.hesil.latteve_spring.domains.member.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import site.hesil.latteve_spring.domains.member.domain.Member;
import site.hesil.latteve_spring.domains.member.dto.request.RequestMember;
import site.hesil.latteve_spring.domains.member.dto.request.UpdateMemberReq;
import site.hesil.latteve_spring.domains.member.dto.response.MemberResponse;
import site.hesil.latteve_spring.domains.member.repository.MemberRepository;
import site.hesil.latteve_spring.domains.member.service.MemberService;
import site.hesil.latteve_spring.global.security.annotation.AuthMemberId;
import site.hesil.latteve_spring.global.security.jwt.TokenService;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * packageName    : site.hesil.latteve_spring.domains.member.controller
 * fileName       : MemberController
 * author         : yunbin
 * date           : 2024-08-26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-26           yunbin           최초 생성
 */

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final TokenService tokenService;

    @PostMapping("/additional-info")
    public ResponseEntity<?> registerAdditionalMemberInfo(@Valid @RequestBody RequestMember requestMember,
                                                          BindingResult bindingResult,
                                                          @AuthenticationPrincipal UserDetails userDetails) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        Optional<Member> _member = memberRepository.findByEmail(userDetails.getUsername());

        if (_member.isPresent()) {
            Member member = _member.get();
            memberService.registerAdditionalMemberInfo(member.getMemberId(), requestMember);
        }

        return ResponseEntity.ok().body("회원 정보가 업데이트되었습니다.");

    }

    @GetMapping("/check-auth")
    public ResponseEntity<Map<String, Boolean>> checkAuth(HttpServletRequest request) {
        // 현재 인증된 사용자가 있는지 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증된 사용자가 있는 경우 로그인 상태로 간주
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated() &&
                !(authentication instanceof AnonymousAuthenticationToken);

        Map<String, Boolean> response = new HashMap<>();
        response.put("isAuthenticated", isAuthenticated);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal UserDetails userDetails, HttpServletResponse response) {
        // access token 쿠키 삭제
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        //cookie.setSecure(true);  // HTTPS를 사용하는 경우
        cookie.setPath("/");
        cookie.setMaxAge(0);  // 쿠키를 즉시 만료시킴
        response.addCookie(cookie);

        tokenService.deleteRefreshToken(userDetails.getUsername());
        //redisMessageService.removeSubscribe(userDetails.getUsername());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/check-nickname")
    public ResponseEntity<Boolean> checkNickname(@RequestBody Map<String, String> request) {
        String nickname = request.get("nickname");

        boolean exists = memberService.checkNickname(nickname);
        log.info(nickname + " " +exists);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/me")
    public ResponseEntity<MemberResponse> getMemberInfo(@AuthMemberId Long memberId) {
        MemberResponse memberResponse = memberService.getMemberInfo(memberId);
        return ResponseEntity.ok(memberResponse);
    }

    // 멤버 정보 수정 API
    @PutMapping("/me")
    public ResponseEntity<String> updateMemberProfile(
            @RequestBody UpdateMemberReq updateRequest,
            @AuthMemberId Long memberId) {

       memberService.updateMemberProfile(memberId, updateRequest);

       return ResponseEntity.ok().body("회원 정보가 업데이트되었습니다.");
    }
}

