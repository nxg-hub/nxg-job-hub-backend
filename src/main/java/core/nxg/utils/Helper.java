package core.nxg.utils;

import core.nxg.configs.JwtService;
import core.nxg.entity.User;
import core.nxg.repository.UserRepository;
import core.nxg.exceptions.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RequiredArgsConstructor
@Service
public class Helper<K, V> {
    private final JwtService jwtService;
    private final UserRepository userRepo;

// <<<<<<< controllers-update
    public User extractLoggedInUser(HttpServletRequest request) {
// =======

//     public final PasswordEncoder encoder = new BCryptPasswordEncoder();

//     public User extractLoggedInUser(HttpServletRequest request){
// >>>>>>> main
        final String authHeader = request.getHeader("Authorization");
        String jwt = authHeader.substring(7);
        String email = jwtService.extractUsername(jwt);
        return userRepo.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public K copyFromDto(V v, K k) {
        BeanUtils.copyProperties(v, k);
        return k;
    }

// <<<<<<< controllers-update
}
// =======
//     // Helper method to get the site URL
//     public String getSiteURL(HttpServletRequest request) {
//         return ServletUriComponentsBuilder.fromRequestUri(request)
//                 .replacePath(null)
//                 .build()
//                 .toString();
//     }

//     // Helper method to encode password
//     public String encodePassword(String password) {

//         return encoder.encode(password);
//     }

// }
// >>>>>>> main
