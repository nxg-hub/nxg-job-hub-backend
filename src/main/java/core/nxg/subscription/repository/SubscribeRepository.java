package core.nxg.subscription.repository;

import core.nxg.subscription.entity.Subscriber;
import core.nxg.subscription.enums.SubscriptionStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscribeRepository extends MongoRepository<Subscriber, String> {

    Subscriber findByCustomerCode(String customerCode);

    Optional<Subscriber> findByEmail(String email);

    Optional<List<Subscriber>> findBySubscriptionStatus(SubscriptionStatus status);





}
