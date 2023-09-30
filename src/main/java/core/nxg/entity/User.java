package core.nxg.entity;

import lombok.*;
import jakarta.persistence.*;

import java.time.LocalDateTime;



import core.nxg.enums.Gender;


@RequiredArgsConstructor
@Entity
@Data
@Table(name = "users")
public class User {
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

    @Column(name = "roles")
    private String roles;


    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private TechTalentUser techTalent;

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private TechTalentAgent techTalentAgent;

     @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Employer employer;


    
}
