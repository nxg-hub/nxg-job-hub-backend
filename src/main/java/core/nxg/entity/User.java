package core.nxg.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import core.nxg.configs.oauth2.OAuth2Provider;
import core.nxg.enums.*;
import lombok.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@RequiredArgsConstructor
@Getter
@Setter
@Document(collection = "users")
public class User implements UserDetails, OAuth2User {
    @Id
    private String id;

    private String firstName;

    private String email;

    private String lastName;

    private String username;

    private String profilePicture;

    private Gender gender;

    private OAuth2Provider provider;

    private long providerId;

    private String phoneNumber;


    private String password;

    private boolean passwordGenerated;


    private String nationality;


    private LocalDate dateOfBirth;


    private Roles roles;

    private UserType userType;


    private LocalDateTime registrationDate;

    private LocalDateTime lastLoginTime;

    private double timeOnPlatform;

    private LocalDate monthJoined;

    private boolean isProfileVerified;


    @JsonIgnore
    @DBRef
    private TechTalentAgent techTalentAgent;



    @JsonIgnore
    @DBRef
    private TechTalentUser techTalent;


    @JsonIgnore
    @DBRef
    private Employer employer;

    @Transient
    private Map<String, Object> attributes;
    @Transient
    private Collection<? extends GrantedAuthority> authorities;


    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }


    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword(){
        return password;
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

    public boolean enabled;
    @Override
    public boolean isEnabled() {

        return enabled;
    }

    // Method to determine profile verification status
    public boolean isProfileVerified() {
    return isProfileVerified;}

    public boolean isPasswordGenerated() {
        return passwordGenerated;}



    @Override
    public String getName() {
        return getFirstName();
    }
}