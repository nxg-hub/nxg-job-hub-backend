package core.nxg.repository;


import java.util.List;
import java.util.Optional;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import core.nxg.entity.Notification;
import core.nxg.entity.User;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
	
	List<Notification> findByReferencedUserOrderByDateTimeDesc(User referencedUser);


	List<Notification> findByReferencedUserId(User id);


	List<Notification> findByReferencedUserAndSeenFalse(User id);

	List<Notification> findByReferencedUserAndDeliveredFalse(User user);


}

