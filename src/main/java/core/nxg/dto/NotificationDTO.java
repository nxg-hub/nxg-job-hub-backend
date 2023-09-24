package core.nxg.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import java.util.Date;
import core.nxg.entity.User;



@Data
@RequiredArgsConstructor

public class NotificationDTO {

    private Long notiificationId;
    private String message;
    private Date timestamp;
    private User user;
}
