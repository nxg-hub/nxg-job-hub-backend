package core.nxg.configs.oauth2;


import core.nxg.configs.SecurityConfiguration;
import core.nxg.entity.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class GitHubOAuth2UserInfoExtractor implements OAuth2UserInfoExtractor {

    @Override
    public User extractUserInfo(OAuth2User oAuth2User) {
        User user = new User();
        user.setEmail(retrieveAttr("email", oAuth2User));
        user.setFirstName(retrieveAttr("name", oAuth2User));
        user.setProfilePicture(retrieveAttr("picture", oAuth2User));
        user.setProvider(OAuth2Provider.GITHUB);
        user.setAttributes(oAuth2User.getAttributes());
        user.setAuthorities(Collections.singletonList(new SimpleGrantedAuthority(SecurityConfiguration.USER)));
        return user;
    }

    @Override
    public boolean accepts(OAuth2UserRequest userRequest) {
        return OAuth2Provider.GITHUB.name().equalsIgnoreCase(userRequest.getClientRegistration().getRegistrationId());
    }


    private String retrieveAttr(String attr, OAuth2User oAuth2User) {
        Object attribute = oAuth2User.getAttributes().get(attr);
        return attribute == null ? "" : attribute.toString();
    }
}
