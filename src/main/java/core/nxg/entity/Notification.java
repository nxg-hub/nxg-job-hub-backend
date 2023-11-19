package core.nxg.entity;

import core.nxg.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@Builder
@Entity
@Table(name = "notifications")
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn(name = "user_id")
	private User referencedUser;
	

	@Column
	private String message;
	
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime dateTime;



	@Column
	private NotificationType notificationType;


	private boolean delivered;

	
	private boolean seen;



}

