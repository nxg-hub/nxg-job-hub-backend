package core.nxg.entity;

import core.nxg.enums.NotificationType;
import core.nxg.enums.SenderType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;



@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document(collection = "notifications")
public class Notification {

	@Id
	private String id;

	private String referencedUserID; // we're using User id for ease. Profile


	private String senderID; 	// picture, fName, lName, would be easy to fetch that way.

							// less db query.

	private String message;
	
//	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime dateTime;


	private Long contentId;


//	umerated(EnumType.STRING)
	private SenderType senderType;

//	umerated(EnumType.STRING)
	private NotificationType notificationType;


	private boolean delivered;

	
	private boolean seen;






}

