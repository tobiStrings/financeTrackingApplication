package com.financeTracker.financeTracker.data.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Data
public class Otp {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String otp;

    @ManyToOne
    private AppUser appUser;

    @CreationTimestamp
    private Date createdAT;
}
