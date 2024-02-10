package core.nxg.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import core.nxg.configs.oauth2.OAuth2Provider;
import core.nxg.enums.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;


@RequiredArgsConstructor
@Entity
@Data
@Table(name = "users")
public class User implements UserDetails, OAuth2User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;


    @Column(nullable = false, unique = true)
    @Email
    private String email;

    private String lastName;

    @Column(name = "username")
    private String username;

    private String profilePicture;


    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private OAuth2Provider provider;

    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long providerId;

    private String phoneNumber;


    @Column(nullable = false)
    private String password;

    @Column(name = "nationality")
    private String nationality;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "roles")
    private String roles;

    @Column(name = "user_type")
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @OneToOne(mappedBy = "agent")
    @JsonIgnore
    private TechTalentAgent techTalentAgent;


    @OneToOne(mappedBy = "techtalent")
    @JsonIgnore
    private TechTalentUser techTalent;

    @OneToOne(mappedBy = "employer")
    @JsonIgnore
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
    public Collection<? extends GrantedAuthority> getAuthorities() {
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

    @Column(name = "enabled")
    public boolean enabled;
    @Override
    public boolean isEnabled() {

        return enabled;
    }


    @Override
    public String getName() {
        return getFirstName();
    }
}