package core.nxg.subscription;

import core.nxg.entity.User;
import core.nxg.exceptions.UserNotFoundException;
import core.nxg.repository.UserRepository;
import core.nxg.subscription.dto.CustomerDTO;
import lombok.RequiredArgsConstructor;
import me.iyanuadelekan.paystackjava.core.Customers;
import me.iyanuadelekan.paystackjava.core.Subscriptions;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    @Autowired
    private final UserRepository userRepo;

    @Autowired
    private final SubscriptionRepository subscriptionRepo;


    public void create(CustomerDTO customerdto) throws Exception{
        User user = userRepo.findByEmail(customerdto.getEmail()).
                orElseThrow(() -> new UserNotFoundException("User not found! Please register first"));
        Customers customers = new Customers();
        JSONObject request = customers.createCustomer(
                customerdto.getEmail(),
                customerdto.getFirstName(),
                customerdto.getLastName(),
                customerdto.getPhone(),
                customerdto.getMetadata());
        if (request.getBoolean("status")) {


            Subscriber subscriber = new Subscriber();
            subscriber.setCustomerId(request.getJSONObject("data").getString("customer_code"));
            subscriber.setEmail(request.getJSONObject("data").getString("email"));
            subscriber.setUser(user);
            userRepo.save(user);
            subscriptionRepo.saveAndFlush(subscriber);
        }
    }
    public void createSubscription(String customer, String plan, String authorization){
        Subscriptions subscriptions = new Subscriptions();
        subscriptions.createSubscription(customer, plan, authorization);
    }
}
