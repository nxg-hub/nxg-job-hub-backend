package core.nxg.utils;

import core.nxg.configs.JwtService;
import core.nxg.entity.User;
import core.nxg.exceptions.ExpiredJWTException;
import core.nxg.exceptions.NotFoundException;
import core.nxg.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URL;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class Helper<K,V> {
    private final JwtService jwtService;
    private final UserRepository userRepo;


    public final PasswordEncoder encoder = new BCryptPasswordEncoder();

    public User extractLoggedInUser(HttpServletRequest request) throws ExpiredJWTException {
        final String authHeader = request.getHeader("Authorization");

        if ( authHeader == null || !authHeader.startsWith("Bearer"))
            throw new ExpiredJWTException("Empty header or invalid jwt");


        String jwt = authHeader.substring(7);
        String email = jwtService.extractUsername(jwt);

        return userRepo.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public K copyFromDto(V source, K target) {
        BeanUtils.copyProperties(source, target);
        return target;
    }

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

    public boolean EmailIsInvalid(String email) {
        Pattern pattern = Pattern.compile("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

        return email == null || !pattern.matcher(email).matches();
    }

    public boolean isPasswordValid(String password, String encodedPassword) {
        return encoder.matches(password, encodedPassword);
    }

    public static String getURLType(String urlString) {
        try {
            URL url = new URL(urlString);
            String host = url.getHost();

            if (host.contains("github.com")) {
                return "GitHub";
            } else if (host.contains("linkedin.com")) {
                return "LinkedIn";
            } else {
                return "Other";
            }
            //TODO: CREATE A CUSTOM EXCEPTION FOR THIS
        } catch (Exception e) {
            return "Invalid URL";
        }
    }
}
