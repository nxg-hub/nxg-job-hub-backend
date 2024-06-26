package core.nxg.controller;

import core.nxg.dto.NotificationDTO;
import core.nxg.entity.Notification;
import core.nxg.service.PushNotifications;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.servlet.function.ServerResponse;
import reactor.core.publisher.Flux;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/notifications")
public class NotificationController {


    @Autowired
    private final PushNotifications service;



    @GetMapping("/stream/{userID}")
    public Flux<ServerSentEvent<List<Notification>>> getNotificationsByUserID(@PathVariable String userID) throws InterruptedException {

        return service.getNotificationsByUserInID(userID);


    }


    @GetMapping("/read/{notifID}")
    public ResponseEntity<?> changeNotifStatusToRead(@PathVariable String notifID) {
        return ResponseEntity.ok(service.changeNotifStatusToRead(notifID));
    }


}