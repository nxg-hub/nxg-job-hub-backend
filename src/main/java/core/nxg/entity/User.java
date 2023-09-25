package core.nxg.entity;
import core.nxg.enums.Gender;
import core.nxg.enums.UserType;
import lombok.*;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;

    private String lastName;

    private String profilePicture; 

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String phoneNumber;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private LocalDateTime dateOfBirth;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private TechTalentUser techTalent;

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private TechTalentAgent techTalentAgent;

     @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Employer employer;







    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
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
        return true;
    }
}
