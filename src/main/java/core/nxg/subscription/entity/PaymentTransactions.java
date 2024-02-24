package core.nxg.subscription.entity;


import core.nxg.subscription.enums.TransactionStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "payment_transactions")
public class PaymentTransactions {

    @Id
    private String transactionReference;

    private TransactionStatus transactionStatus;

    private String transactionMessage;

    private String transactionType;



}
