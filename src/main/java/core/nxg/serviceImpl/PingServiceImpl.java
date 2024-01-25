package core.nxg.serviceImpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class PingServiceImpl  {

    @Value("${server.url}")
    private String serverUrl;  // Use your server URL here
    @Scheduled(fixedRate = 1000000)  // 10 minutes in milliseconds
    public void pingServer() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getForObject(serverUrl, String.class);
            System.out.println("Server pinged at " + System.currentTimeMillis());
        } catch (Exception e) {
            System.err.println("Error while pinging the server: " + e.getMessage());
        }
    }
}
