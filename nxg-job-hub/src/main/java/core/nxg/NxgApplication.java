package core.nxg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableAutoConfiguration
public class NxgApplication {

	public static void main(String[] args) {
		SpringApplication.run(NxgApplication.class, args);
	}
	@Bean
	public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}

}
