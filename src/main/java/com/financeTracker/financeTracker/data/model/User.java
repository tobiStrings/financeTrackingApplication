package com.financeTracker.financeTracker.data.model;

import com.financeTracker.financeTracker.data.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String email;
    private String password;
    private String username;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private boolean isEnabled;
    private Role role;

    @CreationTimestamp
    private Date createdDate;
    @UpdateTimestamp
    private Date updatedDate;
}
