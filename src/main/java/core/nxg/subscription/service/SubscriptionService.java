package core.nxg.subscription.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import core.nxg.entity.User;
import core.nxg.exceptions.UserNotFoundException;
import core.nxg.repository.UserRepository;
import core.nxg.subscription.SubscriptionRepository;
import core.nxg.subscription.dto.CustomerDTO;
import core.nxg.subscription.dto.SubscribeDTO;
import core.nxg.subscription.dto.TransactionDTO;
import core.nxg.subscription.dto.VerificationDTO;
import core.nxg.subscription.entity.Subscriber;
import core.nxg.subscription.enums.PlanType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


@Slf4j
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
                subscriber.setCustomerId(response.getBody().get("data").get("customer_code").asText());
                subscriber.setEmail(response.getBody().get("data").get("email").asText());
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
        try {
            return apiService.initialize(dto);
        } catch (Exception e) {
            Logger.getLogger(SubscriptionService.class.getName())
                    .log(
                            Level.SEVERE, "Could not initialize transaction for " + dto.getEmail());
            throw new Exception(e.getMessage());

        }
    }

    private JsonNode createPlan(Map<String, Object> query) throws Exception {
        try {
            return apiService.plan(query).getBody();
        } catch (Exception e) {
            Logger.getLogger(SubscriptionService.class.getName())
                    .log(
                            Level.SEVERE, "Could not create  " + query.get("name") + " plan");
            throw new Exception(e.getMessage());

        }

    }


    public JsonNode subscribe(SubscribeDTO dto) throws Exception {

        var subscriber = subscriptionRepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("A Subscription Account is not bound to this email!"));
        JsonNode response;

        try {
            log.info("Creating a " + subscriber.getPlanType()+ " plan...");
            response = createPlan(setPlan(subscriber.getPlanType()));
            log.info("Created a " + subscriber.getPlanType() + " plan..." + response);

            TransactionDTO transactionDTO = new TransactionDTO();
            transactionDTO.setEmail(dto.getEmail());
            log.info("Initializing transaction...");
            transactionDTO.setPlan(response.get("data").get("plan_code").asText());
            log.info("Initialized transaction..." + transactionDTO.getPlan());
            transactionDTO.setCallback_url(dto.getCallback_url());
            transactionDTO.setAmount(response.get("data").get("amount").asInt());
            log.info("Initialized transaction..." + transactionDTO.getAmount());

            return this.initializeTransaction(transactionDTO);
        } catch (Exception e) {
            Logger.getLogger(SubscriptionService.class.getName())
                    .log(
                            Level.SEVERE, "Could not initialize transaction @subscription " + dto.getEmail());
            throw new Exception(e.getMessage());

        }

    }

    private Map<String, Object> setPlan(PlanType planType) throws Exception{
        int amount;
        String name;
        String interval;
//        String currency = "NGN"; // removed currency to check if user can specify the currency.
                                    ;//based on the currency { usd,ngn } integration bound to the paystack key
        String description;

        switch (planType) {
            case PLATINUM:
                amount = 10000;
                name = "Platinum";
                interval = "yearly";
                description = "Platinum Subscription Plan";
                break;
            case GOLD:
                amount = 70000;
                name = "Gold";
                interval = "quarterly";
                description = "Gold Subscription Plan";
                break;
            case SILVER:
                amount = 120000;
                name = "Silver";
                interval = "monthly";
                description = "Silver Subscription Plan";
                break;
            default:
                throw new Exception("Invalid plan type");
        }

        Map<String, Object> query = new HashMap<>();
        query.put("name", name);
        query.put("amount", amount);
        query.put("interval", interval);
//        query.put("currency", currency); //to be tested in live environment. See update above.
        query.put("description", description);

        return query;
    }


    public JsonNode validateCustomer(VerificationDTO dto) {
        var user = userRepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found! Please register first"));
        var customer = subscriptionRepo.findByEmail(dto.getEmail())
                .orElseThrow(()-> new UserNotFoundException("A Subscription Account is not bound to this email!"));



        Map<String, Object> request = new HashMap<>();
        request.put("account_number", dto.getAccount_number());
        request.put("bank_code", dto.getBank_code());
        request.put("bvn", dto.getBvn());
        request.put("first_name",user.getFirstName());
        request.put("last_name",user.getLastName() );
        request.put("type","bank_account");
        request.put("country", "NG");
        return apiService.validateIdentity(request,dto.getCustomer_code());





    }
}

