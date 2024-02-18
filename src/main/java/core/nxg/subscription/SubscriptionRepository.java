package core.nxg.subscription;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionRepository  extends JpaRepository<Subscriber, String> {

    Subscriber findByCustomerId(String customerId);

    Optional<Subscriber> findByEmail(String email);






}
