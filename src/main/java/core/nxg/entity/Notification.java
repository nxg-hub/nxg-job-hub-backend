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

	@Getter
	private Long referencedUserID;



	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private User sender ;

	@Getter
	private Long senderID;


	public void setSenderID(){
		this.senderID = sender.getId();
	}

	public void setReferencedUserID(){
		this.referencedUserID = referencedUser.getId();
	}






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

