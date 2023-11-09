package core.nxg.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import core.nxg.enums.Gender;
import core.nxg.enums.IndustryType;
import core.nxg.enums.JobType;
import core.nxg.enums.UserType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;


@RequiredArgsConstructor
@Entity
@Data
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

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "roles")
    private String roles;

    @Column(name = "user_type")
    @Enumerated(EnumType.STRING)
    private UserType userType;


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


}