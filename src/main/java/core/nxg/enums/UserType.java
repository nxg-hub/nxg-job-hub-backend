package core.nxg.enums;
import org.springframework.security.core.GrantedAuthority;

public enum UserType implements GrantedAuthority {
    TECHTALENT,
    AGENT,
    EMPLOYER,
    ADMIN;
    
    @Override
    public String getAuthority() {
        return name();
    }

}