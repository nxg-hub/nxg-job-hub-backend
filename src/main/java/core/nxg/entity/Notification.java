package core.nxg.entity;

import core.nxg.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Data
@RequiredArgsConstructor
@Builder
@Entity
@Table(name = "notifications")
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ref_user")
	private User referencedUser;
	

	@Column
	private String message;
	
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime dateTime;



	@Column
	private NotificationType notificationType;


	private boolean delivered;

	
	private Boolean seen;



}

