package com.project.ReferralTracking.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String referralCode;
    private boolean profileCompleted;

    @ManyToOne
    @JoinColumn(name = "referrer_id")
    private User referrer;

    @Version  // Helps with concurrency issues
    private Integer version;
}
