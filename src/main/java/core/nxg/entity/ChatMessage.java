package core.nxg.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@Document(collection = "chat_message")
public class ChatMessage {
    @Id
    private String content;
    private String sender;
    private String receiver;
}
