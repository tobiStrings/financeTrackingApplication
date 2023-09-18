package com.financeTracker.financeTracker.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.financeTracker.financeTracker.data.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class UserPrincipal implements UserDetails {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    private String phoneNumber;
    @JsonIgnore
    private String password;
    private boolean isEnabled;

    private Collection<? extends GrantedAuthority>  authorities;

    public UserPrincipal(Long id, String firstName, String email, String lastName,
                         String username, String password, boolean isEnabled, String phoneNumber, Collection<? extends GrantedAuthority>  authorities){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.isEnabled = isEnabled;
        this.authorities = authorities;
    }

    public  static UserPrincipal create(User user){
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(user.getRole().name())
        );

        return new UserPrincipal(user.getId(),user.getFirstName(),user.getEmail(),user.getLastName(), user.getUsername(),
                user.getPassword(),user.isEnabled(),user.getPhoneNumber(),authorities);
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
