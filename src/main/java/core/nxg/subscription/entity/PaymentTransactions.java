package core.nxg.subscription.entity;

import org.springframework.data.annotation.Id;
import core.nxg.subscription.enums.TransactionStatus;
import core.nxg.subscription.enums.TransactionType;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.time.LocalTime;


@Getter
@Setter
@NoArgsConstructor(force = true)
@Document(collection = "payment_transactions")
public class PaymentTransactions {

    @Id
    private String Id;

    @NonNull
    private String transactionReference;


    private TransactionStatus transactionStatus;

    private double transactionAmount;




    private LocalDate transactionDate;


    private LocalTime transactionTime;

    private String transactionMessage;



    private TransactionType transactionType;


    private Subscriber subscriber;



}
