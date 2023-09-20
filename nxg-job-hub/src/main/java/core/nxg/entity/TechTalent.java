package core.nxg.entity;

import jakarta.persistence.Column;
/// import core.nxg.entity.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
//import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor

@Entity
(name = "tech_talent")
public class TechTalent extends User{

    private String Nationality;
    private String CountryCode;
    private String City;
    private String State;
    private String residentialAddress;
    private String zipCode;
    private String location;
    private String phoneNumber;
    private String highestQualification, professionalCert, currentJob;
    private String yearsOfExperience;
    private String skills, jobType, workMode;
    
    @Lob
    @Column(name = "resume", columnDefinition = "BLOB")
    private byte[] resume;

    
;   ///public TechTalent() {
        ///super();
       /// this.setUserType(Role.TECHTALENT);
    ///}
 
    
    
    

    @Override
    public boolean isAccountNonExpired() {
        throw new UnsupportedOperationException("Unimplemented method 'isAccountNonExpired'");
    }

    @Override
    public boolean isAccountNonLocked() {
        throw new UnsupportedOperationException("Unimplemented method 'isAccountNonLocked'");
    }

    @Override
    public boolean isCredentialsNonExpired() {
        throw new UnsupportedOperationException("Unimplemented method 'isCredentialsNonExpired'");
    }

    @Override
    public boolean isEnabled() {
        throw new UnsupportedOperationException("Unimplemented method 'isEnabled'");
    }






    
}
