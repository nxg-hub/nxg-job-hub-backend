package core.nxg.entity;
import core.nxg.enums.Gender;
import core.nxg.enums.UserType;
import lombok.*;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Date;
import java.util.Collection;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 20)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 20)
    private String lastName;

    private String username;

    private String profilePicture; 


    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 250)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private TechTalentUser techTalent;


///////////////////////////////////////////////////////
//////////////////////////////////////////////////////
    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private TechTalentAgent techTalentAgent;

     @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    ///private Employer employer;
//////////////////////////////////////////////
//////////////////////////////////////////////

    @Column(name = "date_of_birth")    
    private Date dateOfBirth;




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
