package core.nxg.entity;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
    private String content;
    private String sender;
    private String receiver;
}
