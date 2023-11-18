package core.nxg.dto;

import core.nxg.enums.NotificationType;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Date;
import core.nxg.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@RequiredArgsConstructor
public class NotificationDTO {

    private String message;
    private String email;
    private NotificationType notificationType;

}
