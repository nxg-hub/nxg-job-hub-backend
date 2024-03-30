package core.nxg.enums;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;


@RequiredArgsConstructor
public enum Roles implements GrantedAuthority {

    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");
    private final String role;


    @Override
    public String getAuthority() {
        return this.role;

    }
}
