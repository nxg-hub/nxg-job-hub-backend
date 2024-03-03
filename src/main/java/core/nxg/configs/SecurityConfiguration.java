package core.nxg.configs;

import core.nxg.configs.oauth2.CustomAuthenticationSuccessHandler;
import core.nxg.configs.oauth2.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static core.nxg.utils.Endpoints.UNSECURED_ENDPOINT;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {


    private final CustomOAuth2UserService customOauth2UserService;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final JwtService jwtService;




    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                .csrf(AbstractHttpConfigurer::disable)
                        .headers(headers -> headers.frameOptions().disable())
                .authorizeHttpRequests(
                        auth -> auth.requestMatchers(UNSECURED_ENDPOINT ).permitAll()
                                .anyRequest().authenticated()
                ).oauth2Login(oauth2Login -> oauth2Login
                                .userInfoEndpoint().userService(customOauth2UserService)
                                .and()
                                .successHandler(customAuthenticationSuccessHandler))
                .sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authenticationProvider(authenticationProvider)


                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
    }

    public static final String USER = "USER";



                                                                                                       


   



}
