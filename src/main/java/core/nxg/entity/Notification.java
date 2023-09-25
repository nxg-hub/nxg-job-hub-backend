package core.nxg.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;


@Data
@RequiredArgsConstructor
@EqualsAndHashCode
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    private String message;

    private Date timestamp;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
