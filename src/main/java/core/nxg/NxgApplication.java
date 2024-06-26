package core.nxg;

import core.nxg.subscription.service.APIService;
import core.nxg.utils.SecretService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@Slf4j
@SpringBootApplication
@EnableScheduling
public class NxgApplication {

	@Autowired
	private SecretService secretService;

	public static void main(String[] args) {


		SpringApplication.run(NxgApplication.class, args);
	}

	@Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}



	@PostConstruct
	public void init(){

		try {
			log.info("Initializing header service");
			secretService.init();
		}catch (Exception ex){

			log.info("Error intializing header service");
		}
	}





}

