package core.nxg.subscription.entity;

import core.nxg.entity.User;
import core.nxg.subscription.enums.PlanType;
import core.nxg.subscription.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

@Entity
@Data
@RequiredArgsConstructor
@Table(name = "customers")
public class Subscriber {

    @Id
    private String customerId;

    private String email;

    @Enumerated(EnumType.STRING)
    private PlanType planType;

    @ManyToOne(targetEntity = User.class)
    private User user;


    @Enumerated(EnumType.STRING)
    private SubscriptionStatus subscriptionStatus;

    @OneToMany(targetEntity = PaymentTransactions.class)
    @CollectionTable(name = "payment_transactions", joinColumns = @JoinColumn(name = "customer_id"))
    private Collection<PaymentTransactions> transactions;


}
