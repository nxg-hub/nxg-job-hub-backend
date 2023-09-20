package core.nxg.entity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collections;





/**
 * The User class represents a user entity with properties such as id, name, email, password, and roles.
 * It is used to map Java objects to database tables using the Java Persistence API (JPA).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode
@Table(name = "users")
public abstract class User implements UserDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "profile_picture")
    private String profilePicture;
    
    @Column(name = "username", nullable = false, length = 20)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    

    @Column(nullable = false, length = 64)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    
    private Role userType;


    
    public User(String profilePicture,
                    String username,
                    String email,
                    String password,
                    Role userType) {
        
        this.email = email;
        this.password = password;
        this.userType = userType;
                   }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority(userType.name());
        return Collections.singletonList(authority);
    }

    

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")
    )
    private List<User> users = new ArrayList<>();
}
