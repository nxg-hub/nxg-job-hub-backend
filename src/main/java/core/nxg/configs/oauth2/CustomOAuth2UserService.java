package core.nxg.configs.oauth2;


import core.nxg.entity.User;
import core.nxg.enums.Roles;
import core.nxg.exceptions.ExpiredJWTException;
import core.nxg.service.EmailService;
import core.nxg.service.UserService;
import core.nxg.utils.Helper;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserService userService;
    private final EmailService emailService;
    private final List<OAuth2UserInfoExtractor> oAuth2UserInfoExtractors;
    private final Helper helper;


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

        int providerPosition;
        if (user.getProvider() != null) {
            OAuth2Provider provider = OAuth2Provider.valueOf(String.valueOf(user.getProvider()));
            providerPosition = provider.getPosition();
        } else {
            providerPosition = -1;
        }

        // Check if the user already exists
        if (userOptional.isEmpty()) {
            // New user
            user1 = new User();
            // Set user roles based on certain conditions
            if (providerPosition == 3) {
                user1.setRoles(Roles.USER); // Set roles for Google OAuth users
            } else {
                // Set default roles for other users
                user1.setRoles(Roles.USER);
            }
            user1.setUsername(user.getEmail());
            String[] name = user.getFirstName().split(" ");
            // Generate a random password for the first-time OAuth users
            String randomPassword = userService.generateOAuthPassword();
            user1.setPassword(helper.encodePassword(randomPassword));
            // Set the flag indicating that the password has been generated
            user1.setPasswordGenerated(true);

            // Populate other user fields
            user1.setEmail(user.getEmail());
            user1.setFirstName(name[0]);
            user1.setLastName(name[1]);
            user1.setProfilePicture(user.getProfilePicture());
            user1.setGender(user.getGender());
            user1.setProvider(user.getProvider());
            user1.setProviderId( providerPosition);
            user1.setPhoneNumber(user.getPhoneNumber());
            user1.setNationality(user.getNationality());
            user1.setDateOfBirth(user.getDateOfBirth());
            user1.setEnabled(true);
            user1.setUserType(user.getUserType());

            // Save the new user
            userService.saveUser(user1);

            // Send login details email
            emailService.sendOAuthUserLoginDetails(user.getFirstName(), user.getEmail(), randomPassword);
        } else {
            // Existing user
            user1 = userOptional.get();

            // If password hasn't been generated before, generate and save it
//            if (!user1.isPasswordGenerated()) {
//                String randomPassword = userService.generateOAuthPassword();
//                user1.setPassword(helper.encodePassword(randomPassword));
//                user1.setPasswordGenerated(true);
//                userService.saveUser(user1);
//
//                // Send login details email
//                emailService.sendOAuthUserLoginDetails(user1.getFirstName(), user1.getEmail(), randomPassword);
//            }
        }

        return user1;
    }
}

