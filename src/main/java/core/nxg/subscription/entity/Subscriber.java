package core.nxg.subscription.entity;

import core.nxg.entity.User;
import core.nxg.subscription.enums.PlanType;
import core.nxg.subscription.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
@Table(name = "customers")
public class Subscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    private String customerCode;

    private String email;

    @Enumerated(EnumType.STRING)
    private PlanType planType;

    @ManyToOne(targetEntity = User.class)
    private User user;


    @Temporal(TemporalType.DATE)
    private LocalDate subscriptionStarts;



    @Enumerated(EnumType.STRING)
    private SubscriptionStatus subscriptionStatus;

    @OneToMany(mappedBy = "subscriber", cascade = CascadeType.ALL)
    private List<PaymentTransactions> transactions;




}
