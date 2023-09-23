package core.nxg.entity;

import jakarta.persistence.*;
import lombok.*;


@Data
@RequiredArgsConstructor
@EqualsAndHashCode
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String message;

    private String timestamp;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
