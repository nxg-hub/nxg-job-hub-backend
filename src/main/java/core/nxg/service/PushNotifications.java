package core.nxg.service;

import core.nxg.dto.NotificationDTO;
import core.nxg.entity.Notification;
import core.nxg.entity.User;
import core.nxg.enums.SenderType;
import core.nxg.exceptions.UserNotFoundException;
import core.nxg.repository.NotificationRepository;
import core.nxg.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Delayed;


@Service
@Slf4j
public class PushNotifications {



    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;





    public void pushNotification(NotificationDTO dto, SenderType senderType){

        var notification = Notification.builder()
                .notificationType(dto.getNotificationType())
                .delivered(false)
                .referencedUserID(dto.getReferencedUserID())
                .senderType(senderType)
                .senderID(dto.getSenderID())
                .dateTime(LocalDateTime.now())
                .message(dto.getMessage())
                .build();
        notificationRepository.save(notification);

    }

    @Async
    protected List<Notification> getNotifs(String userID) {


        var notifications = notificationRepository.findByReferencedUserIDAndDeliveredFalse(Long.valueOf(userID));
        notifications.forEach(x -> x.setDelivered(true));
        notificationRepository.saveAll(notifications);
        return notifications;
    }




    public Flux<ServerSentEvent<List<Notification>>> getNotificationsByUserInID(String userID) throws InterruptedException {
        if (userID != null && !userID.isBlank()) {
            return Flux.interval(Duration.ofSeconds(3))
                    .publishOn(Schedulers.boundedElastic())
                    .map(sequence -> ServerSentEvent.<List<Notification>>builder().id(String.valueOf(sequence))
                            .event("notifications").data(getNotifs(userID))
                            .build());
        }

        return Flux.interval(Duration.ofSeconds(3)).map(sequence -> ServerSentEvent.<List<Notification>>builder()
                .id(String.valueOf(sequence)).event("notifications").data(new ArrayList<>()).build());
    }


    public Notification changeNotifStatusToRead(String notifID) {
        var notification = notificationRepository.findById(notifID)
                .orElseThrow(() -> new RuntimeException("No Notifications at the moment!"));
        notification.setSeen(true);
        return notificationRepository.save(notification);
    }




    public void clear() {
            notificationRepository.deleteAll();
        }
    }

