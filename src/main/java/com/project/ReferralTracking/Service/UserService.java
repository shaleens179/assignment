package com.project.ReferralTracking.Service;

import com.project.ReferralTracking.Entity.User;
import com.project.ReferralTracking.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public Map<String, String> signUp(String email, String password, String referralCode) {
        if (userRepository.existsByEmail(email)) {
            return Collections.singletonMap("error", "User already exists");
        }

        // Find referrer if referral code is provided
        User referrer = null;
        if (referralCode != null && !referralCode.isEmpty()) {
            referrer = userRepository.findByReferralCode(referralCode);
            if (referrer == null) {
                return Collections.singletonMap("error", "Invalid referral code");
            }
        }

        // Create new user with unique referral code
        User newUser = User.builder()
                .email(email)
                .password(password)
                .referralCode(UUID.randomUUID().toString())
                .referrer(referrer)
                .profileCompleted(false) // Initially set profile completion as false
                .build();

        userRepository.save(newUser);
        return Collections.singletonMap("message", "Signup successful");
    }

    @Transactional
    public Map<String, String> completeProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (user.isProfileCompleted()) {
            return Collections.singletonMap("message", "Profile is already completed");
        }

        // Mark profile as completed
        user.setProfileCompleted(true);
        userRepository.save(user);

        // If user was referred, mark the referral as successful
        if (user.getReferrer() != null) {
            return Collections.singletonMap("message", "Profile completed, referral successful");
        }

        return Collections.singletonMap("message", "Profile completed successfully");
    }

    public List<User> getReferrals(String referralCode) {
        User referrer = userRepository.findByReferralCode(referralCode);
        if (referrer == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid referral code");
        }
        return userRepository.findByReferrer(referrer);
    }

    public Map<String, Object> getReferralReport() {
        List<User> users = userRepository.findAll();
        Map<String, Object> report = new HashMap<>();

        for (User user : users) {
            List<User> referrals = userRepository.findByReferrer(user);
            List<String> referralEmails = new ArrayList<>();
            for (User ref : referrals) {
                referralEmails.add(ref.getEmail());
            }
            report.put(user.getEmail(), referralEmails);
        }

        return report;
    }
}
