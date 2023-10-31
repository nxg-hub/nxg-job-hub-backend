package core.nxg.utils;

import core.nxg.configs.JwtService;
import core.nxg.entity.User;
import core.nxg.exceptions.NotFoundException;
import core.nxg.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RequiredArgsConstructor
@Service
public class Helper<K,V> {
    private final JwtService jwtService;
    private final UserRepository userRepo;


    public final PasswordEncoder encoder = new BCryptPasswordEncoder();
    // Helper method to get logged in user


    public User extractLoggedInUser(HttpServletRequest request){
        final String authHeader = request.getHeader("Authorization");
        String jwt = authHeader.substring(7);
        String email = jwtService.extractUsername(jwt);
        return userRepo.findByEmail(email).orElseThrow(()-> new NotFoundException("User not found"));

    }
    public String extractLoggedInStringUser(HttpServletRequest request){
        final String authHeader = request.getHeader("Authorization");
        String jwt = authHeader.substring(7);
        String email = jwtService.extractUsername(jwt);
        User user = userRepo.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
        return user.getEmail();
    }

    // Helper method to copy properties from dto to entity
    public K copyFromDto(V v, K k){
        BeanUtils.copyProperties(v, k);
        return k;
    }

    // Helper method to get the site URL
    public String getSiteURL(HttpServletRequest request) {
        return ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(null)
                .build()
                .toString();
    }

    // Helper method to encode password
    public String encodePassword(String password) {

        return encoder.encode(password);
    }
    

}