package core.nxg.repository;


import java.util.List;
import java.util.Optional;

import lombok.NonNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import core.nxg.entity.Notification;
import core.nxg.entity.User;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
	
	List<Notification> findByReferencedUserIDOrderByDateTimeDesc(Long referencedUserID);


	List<Notification> findByReferencedUserID(Long userID);


	List<Notification> findByReferencedUserIDAndSeenFalse(Long userID);

	List<Notification> findByReferencedUserIDAndDeliveredFalse(String referencedUserID);


}

