package core.nxg.enums;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;



public enum Roles implements GrantedAuthority {

    USER,
    ADMIN;


    @Override
    public String getAuthority() {
        return name();
    }
}
