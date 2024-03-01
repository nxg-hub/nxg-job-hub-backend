package core.nxg.configs.oauth2;


import core.nxg.entity.User;
import core.nxg.exceptions.ExpiredJWTException;
import core.nxg.service.EmailService;
import core.nxg.service.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserService userService;
    private final EmailService emailService;
    private final List<OAuth2UserInfoExtractor> oAuth2UserInfoExtractors;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Optional<OAuth2UserInfoExtractor> oAuth2UserInfoExtractorOptional = oAuth2UserInfoExtractors.stream()
                .filter(oAuth2UserInfoExtractor -> oAuth2UserInfoExtractor.accepts(userRequest))
                .findFirst();
        if (oAuth2UserInfoExtractorOptional.isEmpty()) {
            throw new InternalAuthenticationServiceException("The OAuth2 provider is not supported yet");
        }

        User customUser = oAuth2UserInfoExtractorOptional.get().extractUserInfo(oAuth2User);
        User user = null;
        try {
            user = upsertUser(customUser);
        } catch (MessagingException | ExpiredJWTException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        customUser.setId(user.getId());
        return (OAuth2User) customUser;
    }

    private User upsertUser(User user) throws MessagingException, ExpiredJWTException, UnsupportedEncodingException {
        Optional<User> userOptional = userService.getUserByUsername(user.getUsername());
        User user1;
        if (userOptional.isEmpty()) {
            user1 = new User();
            user1.setFirstName(user.getFirstName());
            user1.setEmail(user.getEmail());
            user1.setLastName(user.getLastName());
            user1.setProfilePicture(user.getProfilePicture());
            user1.setGender(user.getGender());
            user1.setProvider(user.getProvider());
            user1.setProviderId(user.getProviderId());
            user1.setPhoneNumber(user.getPhoneNumber());
            user1.setPassword(userService.generateOAuthPassword());
            user1.setNationality(user.getNationality());
            user1.setDateOfBirth(user.getDateOfBirth());
            user1.setEnabled(true);
            user1.setUserType(user.getUserType());
        } else {
            user1 = userOptional.get();
            user1.setEmail(user1.getEmail());
            user1.setProfilePicture(user1.getProfilePicture());
        }
        emailService.sendOAuthUSerLoginDetails(user.getEmail());
        return userService.saveUser(user1);
    }
}
