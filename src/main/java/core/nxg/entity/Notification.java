package core.nxg.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Data
@RequiredArgsConstructor
@EqualsAndHashCode
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
	private Boolean seen;
	

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}

