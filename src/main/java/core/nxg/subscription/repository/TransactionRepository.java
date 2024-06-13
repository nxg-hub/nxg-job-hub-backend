package core.nxg.subscription.repository;

import core.nxg.subscription.entity.PaymentTransactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<PaymentTransactions, Long> {
    PaymentTransactions findByTransactionReference(String transactionReference);
}
