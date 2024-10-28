package com.bit.muiu.controller;

import com.bit.muiu.dto.MemberDto;
import com.bit.muiu.dto.ResponseDto;
import com.bit.muiu.entity.Member;
import com.bit.muiu.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/username-check")
    public ResponseEntity<?> usernameCheck(@RequestBody MemberDto memberDto) {
        ResponseDto<Map<String, String>> responseDto = new ResponseDto<>();

        try {
            log.info("username: {}", memberDto.getUsername());
            Map<String, String> map = memberService.usernameCheck(memberDto.getUsername());

            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("ok");
            responseDto.setItem(map);

            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            log.error("username-check error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody MemberDto memberDto) {
        ResponseDto<MemberDto> responseDto = new ResponseDto<>();

        try {
            log.info("join memberDto: {}", memberDto.toString());
            MemberDto joinedMemberDto = memberService.join(memberDto);

            responseDto.setStatusCode(HttpStatus.CREATED.value());
            responseDto.setStatusMessage("created");
            responseDto.setItem(joinedMemberDto);

            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            log.error("join error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberDto memberDto) {
        ResponseDto<MemberDto> responseDto = new ResponseDto<>();

        try {
            log.info("login memberDto: {}", memberDto.toString());
            MemberDto loginMemberDto = memberService.login(memberDto);

            log.info("loginMembertoken: {}", loginMemberDto.getToken());

            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("ok");
            responseDto.setItem(loginMemberDto);

            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            log.error("login error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout() {
        ResponseDto<Map<String, String>> responseDto = new ResponseDto<>();

        try {
            Map<String, String> logoutMsgMap = new HashMap<>();

            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(null);
            SecurityContextHolder.setContext(securityContext);

            logoutMsgMap.put("logoutMsg", "logout success");

            responseDto.setStatusCode(200);
            responseDto.setStatusMessage("ok");
            responseDto.setItem(logoutMsgMap);

            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            log.error("logout error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        ResponseDto<MemberDto> responseDto = new ResponseDto<>();

        try {
            log.info("Fetching user by ID: {}", id);
            MemberDto memberDto = memberService.getUserById(id);

            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("ok");
            responseDto.setItem(memberDto);

            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            log.error("Error fetching user by ID: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }

    @GetMapping("/{id}/name-role")
    public ResponseEntity<?> getUserInfoById(@PathVariable Long id) {
        try {
            MemberDto memberDto = memberService.getUsernameAndRoleById(id);
            ResponseDto<MemberDto> responseDto = new ResponseDto<>();
            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("ok");
            responseDto.setItem(memberDto);
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            log.error("Error fetching user info by ID: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching user info");
        }
    }

    @GetMapping("/{id}/name")
    public ResponseEntity<?> getNameById(@PathVariable Long id) {
        try{
            MemberDto memberDto = memberService.getNameById(id);
            ResponseDto<MemberDto> responseDto = new ResponseDto<>();
            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("ok");
            responseDto.setItem(memberDto);
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            log.error("Error fetching user name by ID: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching user name");
        }
    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<?> changePassword(@PathVariable Long id, @RequestBody Map<String, String> passwordMap) {
        ResponseDto<String> responseDto = new ResponseDto<>();
        try {
            String currentPassword = passwordMap.get("currentPassword");
            String newPassword = passwordMap.get("newPassword");

            memberService.changePassword(id, currentPassword, newPassword);
            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("Password updated successfully.");

            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            log.error("Password change error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage("An unexpected error occurred.");
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }


    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        ResponseDto<String> responseDto = new ResponseDto<>();
        try {
            memberService.deleteUser(id);
            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setStatusMessage("User deleted successfully.");
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            log.error("User deletion error: {}", e.getMessage());
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setStatusMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(responseDto);
        }
    }

    @GetMapping("/naveruser")
    public ResponseEntity<Member> getNaverUserInfo(@RequestHeader("Authorization") String token) {
        // Authorization 헤더에서 'Bearer ' 부분을 제거하고 토큰만 추출
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);  // 'Bearer ' 이후의 토큰만 추출
        }

        try {
            // token을 사용해 사용자 정보를 가져오는 로직
            Member member = memberService.getMemberByToken(token);  // 이 메서드가 토큰 검증 및 사용자 정보 반환

            if (member == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            return ResponseEntity.ok(member);
        } catch (Exception e) {
            log.error("Error fetching user with token: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/counselNum/{verifyNumber}")
    public ResponseEntity<?> verifyNum(@PathVariable String verifyNumber) {
        try {
            // memberService의 isEqual 메서드가 true/false를 반환
            boolean isVerified = memberService.isEqual(verifyNumber);

            return ResponseEntity.ok(isVerified);
        } catch (Exception e) {
            log.error("Compare error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }
}
