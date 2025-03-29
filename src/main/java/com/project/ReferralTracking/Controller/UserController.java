package com.project.ReferralTracking.Controller;


import com.project.ReferralTracking.Service.UserService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public Map<String, String> signUp(@RequestParam String email,
                                      @RequestParam String password,
                                      @RequestParam(required = false) String referralCode) {
        return userService.signUp(email, password,referralCode);
    }

    @PostMapping("/complete-profile")
    public Map<String, String> completeProfile(@RequestParam String email) {
        return userService.completeProfile(email);
    }

    @GetMapping("/referrals")
    public ResponseEntity<?> getReferrals(@RequestParam String referralCode) {
        if (referralCode == null || referralCode.isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Referral code is required"));
        }
        return ResponseEntity.ok(userService.getReferrals(referralCode));
    }
}
