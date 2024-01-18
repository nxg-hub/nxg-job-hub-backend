package core.nxg.subscription;

import com.fasterxml.jackson.databind.JsonNode;
import core.nxg.entity.User;
import core.nxg.exceptions.UserNotFoundException;
import core.nxg.repository.UserRepository;
import core.nxg.subscription.dto.CustomerDTO;
import lombok.RequiredArgsConstructor;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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

    @Value("${paystack.secret.active}")
    private String API_KEY;


    public JsonNode createSubscriber(CustomerDTO customerdto) throws Exception {
        try {
            User user = userRepo.findByEmail(customerdto.getEmail()).
                    orElseThrow(() -> new UserNotFoundException("User not found! Please register first"));


            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + API_KEY);
            HttpEntity<CustomerDTO> request = new HttpEntity<>(customerdto, headers);


            ResponseEntity<JsonNode> response = restTemplate.exchange(
                    APIConstants.PAYSTACK_CUSTOMER_URL,
                    HttpMethod.POST,
                    request,
                    JsonNode.class);

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
                Logger.getLogger(SubscriptionService.class.getName()).log(Level.SEVERE, "Could not create account for " + customerdto.getEmail());
                throw new Exception("Could not create account for " + customerdto.getEmail());

            }
        }catch (Exception ex) {
            Logger.getLogger(SubscriptionService.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        }

    }



    public JsonNode chargeSubscriber(PlanType planType, String reference, String amount,  // charge the subscriber based on the plan
                                     String email,
                                     String plan, String callback_url){


        if (planType.equals(PlanType.GOLD))
            amount = "50";
        else if (planType.equals(PlanType.SILVER)) {

            amount = "20";
        }
        else
        init(reference,
                amount,
                email,
                plan,
                callback_url);

        return null;
    }

    private void init(String reference, String amount, String email,  // initialize a transaction first.
                      String plan, String callback_url){
        Map<String, Object> content = new HashMap<>();
        content.put("reference", reference);
        content.put("amount", amount);
        content.put("email", email);
        content.put("plan", plan) ; // todo: make "plan" set implicitly by server. not from request to server.
        content.put("callback_url", callback_url );
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + API_KEY);
        HttpEntity<Map<String,Object>> request = new HttpEntity<>(content,headers);

        ResponseEntity<JsonNode> response = restTemplate.exchange(
                APIConstants.PAYSTACK_INIT_TRANSACTIONS,
                HttpMethod.POST,
                request,
                JsonNode.class
        );


    }

}
