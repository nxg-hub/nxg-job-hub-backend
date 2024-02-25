package core.nxg.subscription.repository;

import core.nxg.subscription.entity.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionRepository  extends JpaRepository<Subscriber, Long> {

    Subscriber findByCustomerCode(String customerCode);

    Optional<Subscriber> findByEmail(String email);






}
