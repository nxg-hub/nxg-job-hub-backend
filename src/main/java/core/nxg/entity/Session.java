package core.nxg.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "sessions")
    @Setter
    @Getter
    public class Session {
    @Id
    private String id;
    private String userId;
    private boolean isActive;
    private LocalDateTime loginTime;


    public Session(String id, String userId) {
        this.id = id;
        this.userId = userId;
        this.isActive = true;
        this.loginTime = LocalDateTime.now();
    }

}