package core.nxg.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import core.nxg.entity.Notification;
import core.nxg.entity.User;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
	
	List<Notification> findByUserOrderByDateTimeDesc(User user);

}

