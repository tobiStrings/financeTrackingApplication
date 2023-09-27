package com.financeTracker.financeTracker.data.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Data
@Entity
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String token;

    @OneToOne
    private AppUser appUser;
    private Instant createdDate;
    private Instant expirationDate;
}
