package core.nxg.subscription;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import core.nxg.subscription.dto.CustomerDTO;
import core.nxg.subscription.dto.TransactionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;




@Service
public class APIService {


    @Value("${paystack.secret.active}")
    private String API_KEY;

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

        String reference = "txID" + System.currentTimeMillis();
        dto.setReference(reference);
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
}
