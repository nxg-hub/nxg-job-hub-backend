package core.nxg.configs.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import core.nxg.configs.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@RequiredArgsConstructor
@Component
@Slf4j
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;

    @Value("${app.oauth2.redirectUri}")
    private String redirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        handle(request, response, authentication);
        super.clearAuthenticationAttributes(request);
    }

    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String targetUrl = redirectUri.isEmpty() ?
                determineTargetUrl(request, response, authentication) : redirectUri;

        String token = jwtService.generateToken((UserDetails) authentication.getPrincipal());
        targetUrl = UriComponentsBuilder.fromUriString(targetUrl).queryParam("authKey", token).build().toUriString();
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }


}
