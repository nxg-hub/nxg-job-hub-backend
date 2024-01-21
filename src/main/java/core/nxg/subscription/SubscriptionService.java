package core.nxg.subscription;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import core.nxg.entity.User;
import core.nxg.exceptions.UserNotFoundException;
import core.nxg.repository.UserRepository;
import core.nxg.subscription.dto.CustomerDTO;
import core.nxg.subscription.dto.SubscribeDTO;
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





    public JsonNode initializeTransaction(TransactionDTO dto) throws Exception {
        try{
            return apiService.initialize(dto);}
        catch (Exception e){
            Logger.getLogger(SubscriptionService.class.getName())
                    .log(
                            Level.SEVERE, "Could not initialize transaction for " + dto.getEmail());
            throw new Exception(e.getMessage());

        }
    }
    private JsonNode createPlan(Map<String, Object> query) throws Exception {
        try {
            return apiService.plan(query).getBody();
        } catch(Exception e){
                Logger.getLogger(SubscriptionService.class.getName())
                        .log(
                                Level.SEVERE, "Could not create  " + query.get("name") + " plan");
                throw new Exception(e.getMessage());

            }

        }



    public JsonNode subscribe(SubscribeDTO dto) throws Exception {
        JsonNode response = null;
        try{

            //TODO: Add plan creation logic here
            if (dto.getPlanType().equals(PlanType.PLATINUM)){
                 response =  createPlan(setPlatinum(dto.getPlanType()));
            } else if (dto.getPlanType().equals(PlanType.GOLD)){
                 response =  createPlan(setGold(dto.getPlanType()));
            } else if (dto.getPlanType().equals(PlanType.SILVER)){
                 response =  createPlan(setSilver(dto.getPlanType()));
            } else {
                throw new Exception("Invalid plan type");
            }
            TransactionDTO transactionDTO = new TransactionDTO();
            transactionDTO.setEmail(dto.getEmail());
            transactionDTO.setPlan(response.get("data").get("plan_code").asText());
//            transactionDTO.setCallback_url(dto.getCallback_url());

            return this.initializeTransaction(transactionDTO);}
        catch (Exception e){
            Logger.getLogger(SubscriptionService.class.getName())
                    .log(
                            Level.SEVERE, "Could not initialize transaction for " + dto.getEmail());
            throw new Exception(e.getMessage());

        }







    }
    private Map<String, Object> setPlatinum(PlanType planType) {
        planType = PlanType.PLATINUM;
        String amount = "700000";
        String name = "Platinum";
        String interval = "yearly";
        String currency = "NGN";
        String description = "Platinum Subscription Plan";
        Map<String, Object> query = new HashMap<>();
        query.put("name", name);
        query.put("amount", amount);
        query.put("interval", interval);
        query.put("currency", currency);
        query.put("description", description);
        return query;
    }
    private Map<String, Object> setSilver(PlanType planType) {
        planType = PlanType.SILVER;
        String amount = "450000";
        String name = "Silver";
        String interval = "monthly";
        String currency = "NGN";
        String description = "Silver Subscription Plan";
        Map<String, Object> query = new HashMap<>();
        query.put("name", name);
        query.put("amount", amount);
        query.put("interval", interval);
        query.put("currency", currency);
        query.put("description", description);
        return query;
    }

    private Map<String, Object> setGold(PlanType planType) {
        planType = PlanType.GOLD;
        String amount = "700000";
        String name = "Gold";
        String interval = "quarterly";
        String currency = "NGN";
        String description = "Gold Subscription Plan";
        Map<String, Object> query = new HashMap<>();
        query.put("name", name);
        query.put("amount", amount);
        query.put("interval", interval);
        query.put("currency", currency);
        query.put("description", description);
        return query;
    }
}
