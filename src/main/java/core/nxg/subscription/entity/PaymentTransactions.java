package core.nxg.subscription.entity;


import core.nxg.subscription.enums.TransactionStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "payment_transactions")
public class PaymentTransactions {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    @NonNull
    private String transactionReference;


    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    private Integer transactionAmount;

    private String transactionCurrency;

    @Temporal(TemporalType.DATE)
    private LocalDate transactionDate;

    @Temporal(TemporalType.TIME)
    private LocalTime transactionTime;

    private String transactionMessage;

    private String transactionType;

    @ManyToOne(targetEntity = Subscriber.class)
    private Subscriber subscriber;



}
