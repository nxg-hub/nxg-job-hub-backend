package core.nxg;

import core.nxg.subscription.service.APIService;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NxgApplication {

	@Autowired
	private APIService apiService;

	public static void main(String[] args) {


		SpringApplication.run(NxgApplication.class, args);
	}

	@Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }





	@Bean
	CommandLineRunner run() throws Exception {


	return args -> {
		apiService.init();


	};
}



}

