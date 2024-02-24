package core.nxg.subscription.controller;


import core.nxg.subscription.enums.EventType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/callbacks")
public class APIController {

    @PostMapping("/event")
    public void event(@RequestBody Map<String, Object> query) {
        if (query.get("event").equals(EventType.CHARGE_SUCCESS.getEvent())) {

            System.out.println("SUCCESSFUL TRANSACTION");

        }
        if (query.get("event").equals(EventType.CHARGE_FAILED)){
            System.out.println("Customer identification failed");
        }
            System.out.println(query);

        }


    }

