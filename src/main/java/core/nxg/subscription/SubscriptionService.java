//package core.nxg.subscription;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import core.nxg.entity.User;
//import core.nxg.exceptions.UserNotFoundException;
//import core.nxg.repository.UserRepository;
//import core.nxg.subscription.dto.CustomerDTO;
//import lombok.RequiredArgsConstructor;
//
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//@Service
//@RequiredArgsConstructor
//public class SubscriptionService {
//
//    @Autowired
//    private final UserRepository userRepo;
//
//    @Autowired
//    private final SubscriptionRepository subscriptionRepo;
//
//    @Value("${paystack.secret.active}")
//    private String API_SECRET_KEY;
//
//
//    public JSONObject createSubscriber(CustomerDTO customerdto) throws Exception {
//        try{
//            User user = userRepo.findByEmail(customerdto.getEmail()).
//                orElseThrow(() -> new UserNotFoundException("User not found! Please register first"));
//
//
//            Map<String, Object> query = new HashMap<>();
//        query.put("email", customerdto.getEmail());
//        query.put("first_name", customerdto.getFirstName());
//        query.put("last_name", customerdto.getLastName());
//        query.put("phone", customerdto.getPhone());
//        query.put("metadata", customerdto.getMetadata());
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + API_SECRET_KEY);
//        HttpEntity<Map<String,Object>> request = new HttpEntity<>(query, headers);
//
//
//        ResponseEntity<JSONObject> response = restTemplate.exchange(
//                APIConstants.CUSTOMER_URL,
//                HttpMethod.POST,
//                request,
//                JSONObject.class);
//
//            if (response.hasBody()) {
//                if (response.getBody().getBoolean("status")) {
//
//                    Subscriber subscriber = new Subscriber();
//                    subscriber.setCustomerId(response.getBody().getJSONObject("data").get("customer_code").toString());
//                    subscriber.setEmail(response.getBody().getJSONObject("data").get("email").toString());
//                    subscriber.setUser(user);
//                    userRepo.save(user);
//                    subscriptionRepo.saveAndFlush(subscriber);
//                    return response.getBody();
//                }
//            } else{
//                return new JSONObject().append("message", "Attempt to create subscriber unsuccessful!");}
//
//        } catch (Exception ex) {
//            Logger.getLogger(SubscriptionService.class.getName()).log(Level.SEVERE, null, ex);
//            throw new RuntimeException(ex.getMessage());
//        }
//
//        return new JSONObject();
//    }
//
//
//
//
//
////    public void createSubscription(String customer, String plan, String authorization){
////        Subscriptions subscriptions = new Subscriptions();
////        subscriptions.createSubscription(customer, plan, authorization);
////    }
//}
