package core.nxg.controller;

import core.nxg.dto.NotificationDTO;
import core.nxg.entity.Notification;
import core.nxg.service.PushNotifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/auth/notifications")
public class NotificationController {


    @Autowired
    PushNotifications service;


    @GetMapping("/{userID}")
    public ResponseEntity<List<Notification>> getNotificationsByUserID(@PathVariable String userID) {
        return ResponseEntity.ok(service.getNotificationsByUserID(userID));
    }

    @PatchMapping("/read/{notifID}")
    public ResponseEntity<?> changeNotifStatusToRead(@PathVariable String notifID) {
        return ResponseEntity.ok(service.changeNotifStatusToRead(notifID));
    }

    @PostMapping("/push")
    public ResponseEntity<?> createNotification(@RequestBody NotificationDTO dto) {
        service.pushNotification(dto);
        return ResponseEntity.ok("Notification created successfully!");
    }


}
