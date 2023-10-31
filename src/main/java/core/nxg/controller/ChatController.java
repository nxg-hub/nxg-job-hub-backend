package core.nxg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/auth")
public class ChatController {

    @GetMapping("/chat")
    public String getChatPage(Model model) {
        return "ChatView";
    }

    
}
