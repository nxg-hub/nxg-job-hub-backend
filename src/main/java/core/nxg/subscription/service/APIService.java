package core.nxg.subscription.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import core.nxg.subscription.enums.APIConstants;
import core.nxg.subscription.dto.CustomerDTO;
import core.nxg.subscription.dto.TransactionDTO;
import core.nxg.subscription.enums.EventType;
import core.nxg.subscription.enums.SubscriptionStatus;
import core.nxg.subscription.repository.SubscriptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class APIService {

    @Value("${psk.secret.active}")
    private String API_KEY;


    private SubscriptionRepository repo;

    public ResponseEntity<JsonNode> createCustomer(CustomerDTO dto) throws JsonProcessingException, HttpClientErrorException {

        return post(dto, APIConstants.PAYSTACK_CUSTOMER_URL);

    }


    public ResponseEntity<JsonNode> plan(Map<String, Object> query) throws JsonProcessingException, HttpClientErrorException {

        return post(query, APIConstants.PAYSTACK_PLANS_CREATE_PLAN);

    }
    public ResponseEntity<JsonNode> createSubscription(CustomerDTO dto) throws JsonProcessingException, HttpClientErrorException {

        return post(dto, APIConstants.PAYSTACK_SUBSCRIPTIONS_CREATE_SUBSCRIPTION);

    }



    public JsonNode initialize(TransactionDTO dto) throws JsonProcessingException, HttpClientErrorException {

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


    public void parseEvents(EventType event, String email) {
        String eventString = event.getEvent();
        System.out.println("Event: " + event);
        switch(eventString){
            case "subscription.create":
               log.info("Subscription event received: {}", event.getEvent());
               repo.findByEmail(email).ifPresent(subscriber -> {
                subscriber.setSubscriptionStatus(SubscriptionStatus.ACTIVE);
                subscriber.setSubscriptionStarts(LocalDate.now());
                repo.save(subscriber);});
                break;
                case "charge.failed":

                log.info("A Charge event received {}", eventString);
                break;
            case "charge.success":
                log.info("A Charge event created {}", eventString);
                break;
            case "subscription.disable":
                System.out.println("Subscription disabled");
                break;
            case "invoice.create":
                System.out.println("Invoice created");
                break;
            case "invoice.update":
                System.out.println("Invoice updated");
                break;
            case "invoice.success":
                System.out.println("Invoice payment failed");
                break;
            case "transfer.success":
                System.out.println("Transfer successful");
                break;
            case "transfer.failed":
                System.out.println("Transfer failed");
                break;
            case "transfer.reversed":
                System.out.println("Transfer reversed");
                break;
            default:
                log.info("An unrecognized event was received: {}", eventString);
                break;
        }


    }
}
