package core.nxg.subscription.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import core.nxg.entity.User;
import core.nxg.subscription.enums.PlanType;
import core.nxg.subscription.enums.SubscriptionStatus;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collection;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@RequiredArgsConstructor
@Document(collection = "customers")
public class Subscriber {

    @Id
    private String Id;

    private String customerCode;

    private String email;

    private PlanType planType;

    private User user;


    private LocalDate subscriptionStarts;

    private LocalDate subscriptionDues;


    private SubscriptionStatus subscriptionStatus;

    @JsonIgnore
    private List<PaymentTransactions> transactions;




}
