package core.nxg.subscription.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import core.nxg.subscription.enums.APIConstants;
import core.nxg.subscription.dto.CustomerDTO;
import core.nxg.subscription.dto.TransactionDTO;
import core.nxg.subscription.enums.PlanType;
import core.nxg.subscription.enums.SubscriptionStatus;
import core.nxg.subscription.repository.SubscribeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class APIService {


    @Value("${psk.secret.active}")
    private String API_KEY;

    @Autowired
    private final SubscribeRepository repo;



    public ResponseEntity<JsonNode> createCustomer(CustomerDTO dto) throws  HttpClientErrorException {

        return post(dto, APIConstants.PAYSTACK_CUSTOMER_URL);

    }


    public ResponseEntity<JsonNode> plan(Map<String, Object> query) throws  HttpClientErrorException {

        return post(query, APIConstants.PAYSTACK_PLANS_CREATE_PLAN);

    }
    public ResponseEntity<JsonNode> createSubscription(CustomerDTO dto) throws  HttpClientErrorException {

        return post(dto, APIConstants.PAYSTACK_SUBSCRIPTIONS_CREATE_SUBSCRIPTION);

    }



    public JsonNode initialize(TransactionDTO dto) throws  HttpClientErrorException {

        return post(dto, APIConstants.PAYSTACK_INIT_TRANSACTIONS).getBody();


    }

    public JsonNode verifyTransaction(String reference) throws JsonProcessingException {

        Map<String, Object> content = new HashMap<>();
        return get(content,
                APIConstants.PAYSTACK_VERIFY_TRANSACTIONS + reference).getBody();


    }

    private <T> ResponseEntity<JsonNode> get(T body,
                                             String url) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new APIErrorHandler());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + API_KEY);
        HttpEntity<T> request = new HttpEntity<>(body, headers);
        return restTemplate.getForEntity(
                url,
                JsonNode.class,
                request);
    }

    private <T> ResponseEntity<JsonNode> post(T body,
                                              String url) {


        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new APIErrorHandler());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + API_KEY);
        HttpEntity<T> request = new HttpEntity<>(body, headers);
        return restTemplate.postForEntity(
                url,
                request,
                JsonNode.class);

    }

    public JsonNode validateIdentity(Map<String,Object> body, String customer_code){
        return post(body,
                APIConstants.PAYSTACK_CUSTOMER_URL+ "/" + customer_code + "/identification" ).getBody();

    }


    public void parseEvents(String event, String email) {
        log.info("Event: {}", event);

        switch(event){
            case "subscription.create":
               log.info("Subscription event to be parsed: {}", event); // we get a subscription create event
               repo.findByEmail(email)
                       .ifPresent(subscriber -> {   // we then mark it as active at that instant .
                subscriber
                        .setSubscriptionStatus(SubscriptionStatus.ACTIVE);
                subscriber
                        .setSubscriptionStarts(LocalDate.now());
                subscriber
                        .setSubscriptionDues(updateDueDate(subscriber.getSubscriptionStarts(), subscriber.getPlanType()));
                repo
                        .save(subscriber);});
                break;

            case "charge.failed":

                log.info("A Charge event received {}", event);
                break;
            case "charge.success":
                log.info("A Charge event successfully created ");
                break;


            case "subscription.disable":
                log.info("Subscription disabled");
                break;
            case "invoice.create":
                log.info("Invoice created");
                break;
            case "invoice.update":
                log.info("Invoice updated");
                break;
            case "invoice.success":
                log.info("Invoice payment failed");
                break;
            case "transfer.success":
                log.info("Transfer successful");
                break;
            case "transfer.failed":
                log.info("Transfer failed");
                break;
            case "transfer.reversed":
                log.info("Transfer reversed");
                break;
            default:
                log.info("An unrecognized event was received: {}", event);
                throw new IllegalStateException("Unexpected value: " + event);

        }


    }
    private LocalDate updateDueDate(LocalDate startDate, PlanType planType) {
        return switch (planType) {
            case SILVER -> startDate.plus(Period.ofMonths(3));
            case  GOLD -> startDate.plus(Period.ofMonths(6));
            case PLATINUM -> startDate.plus(Period.ofMonths(12));
            case FREE -> startDate.plusMonths(1);
        };
    }
}
