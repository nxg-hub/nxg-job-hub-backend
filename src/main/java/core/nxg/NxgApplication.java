package core.nxg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@SpringBootApplication
@ComponentScan("core.nxg")
@EnableAutoConfiguration
public class NxgApplication {

	public static void main(String[] args) {
		SpringApplication.run(NxgApplication.class, args);
	}


	@Configuration
	public static class MailConfiguration {


		 @Bean
		 public JavaMailSender javaMailSender() {
		     JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		     mailSender.setHost("smtp.gmail.com");
		     mailSender.setPort(587);
		     mailSender.setUsername("abayomioluwatimilehinstephen@gmail.com");
		     mailSender.setPassword("ezpmgdgbtizyxdjg");
		     Properties props = mailSender.getJavaMailProperties();
		     props.put("mail.transport.protocol", "smtp");
		     props.put("mail.smtp.auth", "true");
		     props.put("mail.smtp.starttls.enable", "true");
		     return mailSender;
		 }
	}
}

