package core.nxg.subscription.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import core.nxg.entity.User;
import core.nxg.exceptions.UserNotFoundException;
import core.nxg.repository.UserRepository;
import core.nxg.subscription.entity.PaymentTransactions;
import core.nxg.subscription.enums.SubscriptionStatus;
import core.nxg.subscription.enums.TransactionStatus;
import core.nxg.subscription.repository.SubscriptionRepository;
import core.nxg.subscription.dto.CustomerDTO;
import core.nxg.subscription.dto.SubscribeDTO;
import core.nxg.subscription.dto.TransactionDTO;
import core.nxg.subscription.dto.VerificationDTO;
import core.nxg.subscription.entity.Subscriber;
import core.nxg.subscription.enums.PlanType;
import core.nxg.subscription.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
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
    private final TransactionRepository transactionRepo;

    @Autowired
    private final APIService apiService;


    public JsonNode createSubscriber(Map<String, Object> arg) throws HttpClientErrorException, JsonProcessingException {
        try {
            User user = userRepo.findByEmail(arg.get("email").toString()).
                    orElseThrow(() -> new UserNotFoundException("User not found! Please register first"));

            CustomerDTO customerdto = new CustomerDTO();
            customerdto.setEmail(arg.get("email").toString());
            customerdto.setFirstName(user.getFirstName());
            customerdto.setLastName(user.getLastName());
            customerdto.setPhone(user.getPhoneNumber());

            ResponseEntity<JsonNode> response = apiService.createCustomer(customerdto);

            if (response.hasBody() && response.getBody().get("status").booleanValue()) {
                var email = response.getBody().get("data").get("email").asText();
                var existing = subscriptionRepo.findByEmail(email);
                existing.ifPresentOrElse
                        (existingsubscriber -> {
                                existingsubscriber.setPlanType(PlanType.valueOf(arg.get("planType").toString().toUpperCase()));
                                subscriptionRepo.save(existingsubscriber);
                         },
                                () -> {
                                    Subscriber subscriber = new Subscriber();
                                       subscriber.setCustomerCode(response.getBody().get("data").get("customer_code").asText());
                                    subscriber.setEmail(email);
                                    subscriber.setPlanType(PlanType.valueOf(arg.get("planType").toString().toUpperCase()));
                                    subscriber.setUser(user);
                                    userRepo.save(user);
                                    subscriptionRepo.saveAndFlush(subscriber);
                                    log.info("Subscriber created successfully");
                                });
                return response.getBody();

            } else {
                Logger.getLogger(SubscriptionService.class.getName())
                        .log(
                                Level.WARNING, "Could not create account for " + customerdto.getEmail());
                throw new Exception("Could not create account for " + customerdto.getEmail());

            }
        } catch (Exception ex) {
            Logger.getLogger(SubscriptionService.class.getName())
                    .log(
                            Level.WARNING, null, ex);
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
        if (subscriber.getCustomerCode() == null) {
            throw new Exception("Customer ID is blank or null!");
        }

        try {
            log.info("Creating a " + subscriber.getPlanType()+ " plan...");
            response = createPlan(setPlan(subscriber.getPlanType()));
            log.info("Created a " + subscriber.getPlanType() + " plan..." + response);
            String reference = "txID" + System.currentTimeMillis();

            TransactionDTO transactionDTO = new TransactionDTO();
            transactionDTO.setEmail(dto.getEmail());
            log.info("Initializing transaction...");
            transactionDTO.setPlan(response.get("data").get("plan_code").asText());
            log.info("Initialized transaction..." + transactionDTO.getPlan());
            transactionDTO.setCallback_url(dto.getCallback_url());
            transactionDTO.setAmount(response.get("data").get("amount").asInt());
            log.info("Initialized transaction..." + transactionDTO.getAmount());
            transactionDTO.setReference(reference);

            PaymentTransactions tranx = new PaymentTransactions();
            tranx.setTransactionReference(reference);
            tranx.setTransactionStatus(TransactionStatus.PENDING);
            tranx.setTransactionAmount(transactionDTO.getAmount());
            tranx.setTransactionMessage(subscriber.getPlanType() + " plan subscription ");
            tranx.setTransactionDate(LocalDate.now());
            tranx.setTransactionTime(LocalTime.now());
            tranx.setSubscriber(subscriber);
            subscriptionRepo.save(subscriber);
            transactionRepo.save(tranx);




            return this.initializeTransaction(transactionDTO);
        } catch (Exception e) {
            Logger.getLogger(SubscriptionService.class.getName())
                    .log(
                            Level.SEVERE, "Could not initialize transaction @subscription " + dto.getEmail(),e);
            throw new Exception("Cannot complete request at this time! Please try again later.");

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
                amount = 900000;
                name = "Platinum";
                interval = planType.getInterval();
                description = "Platinum Subscription Plan";
                break;
            case GOLD:
                amount = 700000;
                name = planType.getInterval();
                interval =
                description = "Gold Subscription Plan";
                break;
            case SILVER:
                amount = 250000;
                name = "Silver";
                interval = planType.getInterval();
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




    @Scheduled(cron = "0 0 0 */5 * *")
    public void updateSubscriptionStatus() {
        log.info("..Checking for active subscribers");
        Optional<List<Subscriber>> activeSubscribers = subscriptionRepo.findBySubscriptionStatus(SubscriptionStatus.ACTIVE);

        if (activeSubscribers.isPresent()) {
            log.info("Found {} active subscribers", activeSubscribers.get().size());
            List<Subscriber> updatedSubscribers = new ArrayList<>() ;
            for (Subscriber subscriber : activeSubscribers.get()) {

                LocalDate endDate = calculateEndDate(subscriber.getSubscriptionStarts(), subscriber.getPlanType());

                if (LocalDate.now().isAfter(endDate)) {
                    log.info("Subscriber {} subscription ends on {}", subscriber.getEmail(), endDate);


                    subscriber.setSubscriptionStatus(SubscriptionStatus.INACTIVE);
                    log.warn("Subscriber {} subscription has expired and set INACTIVE", subscriber.getEmail());
                    updatedSubscribers.add(subscriber);



                }
            }
            subscriptionRepo.saveAll(updatedSubscribers);
        }
    }

    private LocalDate calculateEndDate(LocalDate startDate, PlanType planType) {
        return switch (planType) {
            case SILVER -> startDate.plus(Period.ofMonths(3));
            case  GOLD -> startDate.plus(Period.ofMonths(6));
            case PLATINUM -> startDate.plus(Period.ofMonths(12));
        };
    }
}

