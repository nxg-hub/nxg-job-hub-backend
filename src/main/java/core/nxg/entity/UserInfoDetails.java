package core.nxg.entity;


//import core.nxg.enums.UserType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.stream.Collectors;
import java.util.Collection;
//import java.util.HashSet;
//import java.util.Map;
import java.util.List;
import java.util.Arrays;

public class UserInfoDetails implements UserDetails{

    private String email;
    private String password;
    private List<GrantedAuthority> authorities;
    //private Map<String, UserType> roleMapping;

    public UserInfoDetails(User userInfo) {
        this.email = userInfo.getEmail();
        this.password = userInfo.getPassword();
        this.authorities = Arrays.stream(userInfo.getRoles().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        ;
        
    }

    






   
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
  
        return authorities;
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

    @Override
    public boolean isEnabled() {
        return true;
    }
}
