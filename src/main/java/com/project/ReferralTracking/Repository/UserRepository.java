package com.project.ReferralTracking.Repository;


import com.project.ReferralTracking.Entity.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    User findByReferralCode(String referralCode);
    List<User> findByReferrer(User referrer);
}