package core.nxg.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import core.nxg.enums.NotificationType;
import core.nxg.enums.UserType;
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private User referencedUser;

	@Setter
	private Long referencedUserID = referencedUser.getId();

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private User sender ;

	@Setter
	private Long senderID = sender.getId(); ;




	@Column
	private String message;
	
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime dateTime;


	private Long contentId;

	@Column
	private NotificationType notificationType;


	private boolean delivered;

	
	private boolean seen;






}

