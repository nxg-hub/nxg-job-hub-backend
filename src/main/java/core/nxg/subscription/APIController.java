package core.nxg.subscription;


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
    public void event(@RequestBody Map<String,Object> query){
        if (query.get("event").equals(EventType.CUSTOMER_IDENTIFICATION_SUCCESS)){
            System.out.println("Customer identification successful");

        }
        if (query.get("event").equals(EventType.CUSTOMER_IDENTIFICATION_FAILED)){
            System.out.println("Customer identification failed");
        }
        System.out.println(query);

    }


}
