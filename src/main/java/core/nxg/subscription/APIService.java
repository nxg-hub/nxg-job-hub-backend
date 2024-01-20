package core.nxg.subscription;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import core.nxg.subscription.dto.CustomerDTO;
import core.nxg.subscription.dto.TransactionDTO;
import lombok.RequiredArgsConstructor;
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
import java.util.logging.Level;
import java.util.logging.Logger;


@Service
@RequiredArgsConstructor
public class APIService {


    @Value("${paystack.secret.active}")
    private String API_KEY;

    private <T> ResponseEntity<JsonNode> connect(T body,
                                                 String url,
                                                 HttpMethod method) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + API_KEY);
        HttpEntity<T> request = new HttpEntity<>(body, headers);
        return restTemplate.exchange(
                url,
                method,
                request,
                JsonNode.class);

    }

    public JsonNode initialize(TransactionDTO dto) throws JsonProcessingException, HttpClientErrorException {

        String reference = "txID" + System.currentTimeMillis();
        dto.setReference(reference);

        return connect(dto, APIConstants.PAYSTACK_INIT_TRANSACTIONS, HttpMethod.POST).getBody();


    }

    public JsonNode verifyTransaction(String reference) throws JsonProcessingException {

        Map<String, Object> content = new HashMap<>();
        return connect(content,
                APIConstants.PAYSTACK_VERIFY_TRANSACTIONS + reference,
                HttpMethod.GET
        ).getBody();


    }
    public ResponseEntity<JsonNode> createCustomer(CustomerDTO dto) throws JsonProcessingException, HttpClientErrorException {

        return connect(dto, APIConstants.PAYSTACK_CUSTOMER_URL, HttpMethod.POST);

    }
}
