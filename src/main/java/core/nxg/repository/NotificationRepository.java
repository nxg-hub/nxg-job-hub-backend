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
	
	List<Notification> findByUserOrderByDateTimeDesc(User referencedUser);


	List<Notification> findByUserId(Long id);


	List<Notification> findByUserIdAndSeenFalse(Long id);


}

