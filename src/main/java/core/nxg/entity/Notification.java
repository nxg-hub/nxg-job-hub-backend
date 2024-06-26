package core.nxg.entity;

import core.nxg.enums.NotificationType;
import core.nxg.enums.SenderType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "notifications")
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private Long referencedUserID; // we're using User id for ease. Profile


	private Long senderID; 	// picture, fName, lName, would be easy to fetch that way.

							// less db query.

	private String message;
	
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime dateTime;


	private Long contentId;


	@Enumerated(EnumType.STRING)
	private SenderType senderType;

	@Enumerated(EnumType.STRING)
	private NotificationType notificationType;


	private boolean delivered;

	
	private boolean seen;






}

