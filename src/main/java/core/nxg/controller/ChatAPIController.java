//package core.nxg.controller;
//
//import core.nxg.entity.ChatMessage;
//
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.web.bind.annotation.*;
//import core.nxg.utils.Helper;
//
//@RestController
//public class ChatAPIController {
//
//    @Autowired
//    SimpMessagingTemplate messagingTemplate;
//
//    private final Logger logger = LoggerFactory.getLogger(ChatAPIController.class);
//
//    @Autowired
//    Helper helper;
//
//
//    @PostMapping("/send")
//    @MessageMapping("/message")
//    @SendTo("/chatroom/public")
//    public ChatMessage sendMessage(@Payload ChatMessage message) throws InterruptedException {
//        logger.atInfo().log("Message sent to chatroom :" + message);
//        return message;
//    }
//
//
//
//    // @MessageMapping("/typing")
//    // @SendTo("/chatroom/public")
//    // public ChatMessage typingMessage(@Payload ChatMessage message) throws InterruptedException {
//    //     logger.atInfo().log("User " + message.getSender() + " is typing...");
//    //     return message;}
//
//        /* IMPLEMENT SENDING TO A UNIQUE USER pRIVATE MESAGES */
//
//    @PostMapping("/send-to")
//    @MessageMapping("/private-message")
//    public ChatMessage sendPrivateMessage(@Payload ChatMessage message) {
//        messagingTemplate.convertAndSendToUser(message.getReceiver(), "/private", message);
//        logger.atInfo().log("Message sent to user :" + message.getReceiver() + "/n Sent..." + message);
//        return message;
//
//    }
//
//
//
//
//}
//
//
