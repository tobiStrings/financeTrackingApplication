package com.financeTracker.financeTracker.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.financeTracker.financeTracker.data.model.AppUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Slf4j
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

//    public UserPrincipal(Long id, String firstName, String email, String lastName,String username,
//                         String password, boolean isEnabled, String phoneNumber, Collection<? extends GrantedAuthority>  authorities){
//        this.id = id;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.username = username;
//        this.email = email;
//        this.password = password;
//        this.phoneNumber = phoneNumber;
//        this.isEnabled = isEnabled;
//        this.authorities = authorities;
//    }
//
//    public  static UserPrincipal create(AppUser appUser){
//        List<GrantedAuthority> authorities = Collections.singletonList(
//                new SimpleGrantedAuthority(appUser.getRole().name())
//        );
//
//        return new UserPrincipal(appUser.getUserId(), appUser.getFirstName(), appUser.getEmail(), appUser.getLastName(),appUser.getUsername(),
//                appUser.getPassword(), appUser.isEnabled(), appUser.getPhoneNumber(),authorities);
//    }

    public UserPrincipal(Long id, String email, String password, boolean enabled, String phoneNumber,String username ,List<GrantedAuthority> authorities) {
        this.id =  id;
        this.email  = email;
        this.password = password;
        this.isEnabled = enabled;
        this.phoneNumber = phoneNumber;
        this.authorities = authorities;
        this.username = username;
    }

    public static UserDetails create(AppUser user) {
        log.info("App user here {}", user);
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()));
        UserPrincipal userPrincipal =  new UserPrincipal(user.getUserId(),
                user.getEmail(), user.getPassword(),user.isEnabled(),user.getPhoneNumber(),user.getUsername(),authorities
        );
        log.info(userPrincipal.getEmail());
        return userPrincipal;
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
