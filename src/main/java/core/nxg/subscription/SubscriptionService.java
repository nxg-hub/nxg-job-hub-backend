package core.nxg.subscription;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import core.nxg.entity.User;
import core.nxg.exceptions.UserNotFoundException;
import core.nxg.repository.UserRepository;
import core.nxg.subscription.dto.CustomerDTO;
import core.nxg.subscription.dto.TransactionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    @Autowired
    private final UserRepository userRepo;

    @Autowired
    private final SubscriptionRepository subscriptionRepo;

    @Autowired
    private final APIService apiService;





    public JsonNode createSubscriber(CustomerDTO customerdto) throws HttpClientErrorException, JsonProcessingException {
        try {
            User user = userRepo.findByEmail(customerdto.getEmail()).
                    orElseThrow(() -> new UserNotFoundException("User not found! Please register first"));



            ResponseEntity<JsonNode> response = apiService.createCustomer(customerdto);

            if (response.hasBody() && response.getBody().get("status").booleanValue()) {

                Subscriber subscriber = new Subscriber();
                subscriber.setCustomerId(response.getBody().get("data").get("customer_code").toString());
                subscriber.setEmail(response.getBody().get("data").get("email").toString());
                subscriber.setPlanType(customerdto.getPlanType());
                subscriber.setUser(user);
                userRepo.save(user);
                subscriptionRepo.saveAndFlush(subscriber);
                return response.getBody();

            } else {
                Logger.getLogger(SubscriptionService.class.getName())
                        .log(
                                Level.SEVERE, "Could not create account for " + customerdto.getEmail());
                throw new Exception("Could not create account for " + customerdto.getEmail());

            }
        } catch (Exception ex) {
            Logger.getLogger(SubscriptionService.class.getName())
                    .log(
                            Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        }

    }





    public JsonNode initializeTransaction(TransactionDTO dto) throws JsonProcessingException, HttpClientErrorException{
        try{

            return apiService.initialize(dto);}
        catch (Exception e){
            Logger.getLogger(SubscriptionService.class.getName())
                    .log(
                            Level.SEVERE, "Could not initialize transaction for " + dto.getEmail());
            throw new RuntimeException("Could not initialize transaction for " + dto.getEmail());

        }
    }







}
