package core.nxg.service;

import core.nxg.dto.NotificationDTO;
import core.nxg.entity.Notification;
import core.nxg.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Delayed;


@Service
@Slf4j
public class PushNotifications {



    @Autowired
    private NotificationRepository notificationRepository;


    public void pushNotification(NotificationDTO dto) {
        var notification =
        Notification.builder()
                .notificationType(dto.getNotificationType())
                .message("You have a new " + dto.getNotificationType().toString() + "!")
                .delivered(false)
                .dateTime(LocalDateTime.now());
        pushNotification(notification.build());
    }
    private void pushNotification(Notification notification) {
        notificationRepository.save(notification);
    }

    private List<Notification> getNotifs(Long userID) {
        var notifications = notificationRepository.findByUserIdAndSeenFalse(userID);
        notifications.forEach(x -> x.setDelivered(true));
        notificationRepository.saveAll(notifications);
        return notifications;
    }


    // DO NOT TOUCH THIS METHOD EXCEPT YOU KNOW WHAT YOU'RE DOING!

    public Flux<ServerSentEvent<List<Notification>>> getNotificationsByUserInID(String userID) throws InterruptedException {
        Thread.sleep(10000);
        if (userID != null && !userID.isBlank()) {
            return Flux.interval(Duration.ofSeconds(1))
                    .publishOn(Schedulers.boundedElastic())
                    .map(sequence -> ServerSentEvent.<List<Notification>>builder().id(String.valueOf(sequence))
                            .event("user-list-event").data(getNotifs(Long.valueOf(userID)))
                            .build());
        }

        return Flux.interval(Duration.ofSeconds(5)).map(sequence -> ServerSentEvent.<List<Notification>>builder()
                .id(String.valueOf(sequence)).event("user-list-event").data(new ArrayList<>()).build());
    }
    public List<Notification> getNotificationsByUserID(String userID) {
        return notificationRepository.findByUserId(Long.valueOf(userID));
    }

    public Notification changeNotifStatusToRead(String notifID) {
        var notification = notificationRepository.findById(Long.valueOf(notifID))
                .orElseThrow(() -> new RuntimeException("No Notifications at the moment!"));
        notification.setSeen(true);
        return notificationRepository.saveAndFlush(notification);
    }

    public List<Notification> getNotificationsByUserIDNotRead(String userID) {
        return notificationRepository.findByUserIdAndSeenFalse(Long.valueOf(userID));
    }


    public void clear() {
            notificationRepository.deleteAll();
        }
    }

