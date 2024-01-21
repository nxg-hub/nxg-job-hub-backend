package core.nxg.subscription;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository  extends JpaRepository<Subscriber, String> {

    Subscriber findByCustomerId(String customerId);




}
