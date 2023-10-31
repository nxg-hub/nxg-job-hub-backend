package core.nxg.controller;

import core.nxg.entity.ChatMessage;
import core.nxg.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import core.nxg.utils.Helper;

@RestController
public class ChatAPIController {

    private final SimpMessagingTemplate messagingTemplate;

    private final Logger logger = LoggerFactory.getLogger(ChatAPIController.class);

    @Autowired
    Helper helper;

    public ChatAPIController(SimpMessagingTemplate messagingTemplate)  {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/message")
    @SendTo("/chatroom/public")    
    public ChatMessage sendMessage(@RequestBody ChatMessage message) throws InterruptedException {
        messagingTemplate.convertAndSend("/chatroom/messages", message);
        logger.atInfo().log("Message sent to chatroom :" + message);
        return message;
    }
  
        /* IMPLEMENT SENDING TO A UNIQUE USER pRIVATE MESAGES */


    @MessageMapping("/direct-message") 
    public ChatMessage sendPrivateMessage(@RequestBody ChatMessage message, HttpServletRequest request) {
        String Sender = helper.extractLoggedInStringUser(request);
        messagingTemplate.convertAndSendToUser(Sender, "/direct", message);
        logger.atInfo().log("Message sent to user :" + Sender + "/n Sent..." + message);
        return message;

    }


    

}

