package core.nxg.utils;

import core.nxg.configs.JwtService;
import core.nxg.entity.User;
import core.nxg.exceptions.NotFoundException;
import core.nxg.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.beans.FeatureDescriptor;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class Helper<K,V> {
    private final JwtService jwtService;
    private final UserRepository userRepo;


    public final PasswordEncoder encoder = new BCryptPasswordEncoder();

    public User extractLoggedInUser(HttpServletRequest request){
        final String authHeader = request.getHeader("Authorization");
        String jwt = authHeader.substring(7);
        String email = jwtService.extractUsername(jwt);
        return userRepo.findByEmail(email).orElseThrow(()-> new NotFoundException("User not found"));
    }

    public K copyFromDto ( V source,K target){
        BeanUtils.copyProperties(source,target);
        return target;
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

    public boolean isEmailValid(String email) {
        Pattern pattern = Pattern.compile("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

        return email != null && pattern.matcher(email).matches();
    }

    public boolean isPasswordValid(String password, String encodedPassword){
        return encoder.matches(password, encodedPassword);
    }

    public Object partialUpdate(Object dbObject, Object partialUpdateObject){
        String[] ignoredProperties = getNullPropertyNames(partialUpdateObject);
        BeanUtils.copyProperties(partialUpdateObject, dbObject, ignoredProperties);
        return dbObject;
    }
    private static String[] getNullPropertyNames(Object object) {
        final BeanWrapper wrappedSource = new BeanWrapperImpl(object);
        return Stream.of(wrappedSource.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null)
                .toArray(String[]::new);
    }

}