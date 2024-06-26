package core.nxg.subscription.entity;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import core.nxg.subscription.enums.TransactionStatus;
import core.nxg.subscription.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(force = true)
@Table(name = "payment_transactions")
public class PaymentTransactions {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    @NonNull
    private String transactionReference;


    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    private double transactionAmount;



    @Temporal(TemporalType.DATE)
    private LocalDate transactionDate;

    @Temporal(TemporalType.TIME)
    private LocalTime transactionTime;

    private String transactionMessage;


    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @ManyToOne(targetEntity = Subscriber.class)
    private Subscriber subscriber;



}
