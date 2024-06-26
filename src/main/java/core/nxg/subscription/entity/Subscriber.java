package core.nxg.subscription.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import core.nxg.entity.User;
import core.nxg.subscription.enums.PlanType;
import core.nxg.subscription.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
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

    @Temporal(TemporalType.DATE)
    private LocalDate subscriptionDues;


    @Enumerated(EnumType.STRING)
    private SubscriptionStatus subscriptionStatus;

    @JsonIgnore
    @OneToMany(mappedBy = "subscriber", cascade = CascadeType.ALL)
    private List<PaymentTransactions> transactions;




}
