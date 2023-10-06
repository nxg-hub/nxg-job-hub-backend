package core.nxg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("core.nxg")
@EnableAutoConfiguration
public class NxgApplication {

	public static void main(String[] args) {
		SpringApplication.run(NxgApplication.class, args);
	}

}
