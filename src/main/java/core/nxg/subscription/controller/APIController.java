package core.nxg.subscription.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.nxg.subscription.enums.EventType;
import core.nxg.subscription.enums.SubscriptionStatus;
import core.nxg.subscription.repository.SubscriptionRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/callbacks")
public class APIController {

    @Autowired
    private SubscriptionRepository repo;

    @PostMapping("/event")
    public void event(@RequestBody Map<String, Object> query,@RequestHeader ("x-paystack-signature") String headerSignature ) throws Exception {


        // todo: validate the signature
        if (headerSignature == null || headerSignature.isEmpty()) {
            throw new Exception("x-paystack-signature header is missing");
        }
        if (query.get("event").equals(EventType.SUBSCRIPTION_CREATE.getEvent())) {

            ObjectMapper mapper = new ObjectMapper();
            TypeReference<JsonNode> typeReference = new TypeReference<>() {
            };
            JsonNode data = mapper.convertValue(query.get("data"), typeReference);

            String email = data.get("customer").get("email").asText();

            repo.findByEmail(email).ifPresent(subscriber -> {
                subscriber.setSubscriptionStatus(SubscriptionStatus.ACTIVE);
                repo.save(subscriber);
            });


        }
    }
}





