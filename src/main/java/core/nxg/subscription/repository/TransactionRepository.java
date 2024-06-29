package core.nxg.subscription.repository;

import core.nxg.subscription.entity.PaymentTransactions;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends MongoRepository<PaymentTransactions, String> {
    PaymentTransactions findByTransactionReference(String transactionReference);
}
