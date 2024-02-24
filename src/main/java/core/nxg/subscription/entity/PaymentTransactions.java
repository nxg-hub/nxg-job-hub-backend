package core.nxg.subscription.entity;


import core.nxg.subscription.enums.TransactionStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "payment_transactions")
public class PaymentTransactions {

    @Id
    private String transactionReference;


    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    private String transactionMessage;

    private String transactionType;



}
