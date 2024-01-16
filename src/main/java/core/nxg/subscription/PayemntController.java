package core.nxg.subscription;


import com.fasterxml.jackson.databind.JsonNode;
import com.nimbusds.jose.shaded.gson.JsonObject;
import core.nxg.subscription.dto.CustomerDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/subscriptions")
public class PayemntController {

    @Autowired
    private final SubscriptionService paymentService;



    @Operation(summary = "Create a subscription account for a user")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Create a new subscription account for a user with an " +
            "email address, first name, " +
            "last name, phone number and metadata *(a key-value pair object type)* ", required = true, content =
    @Content(mediaType = "application/json",
            schema = @Schema(implementation = CustomerDTO.class)))
    @PostMapping("/create-account")
    public ResponseEntity<JsonNode> createAccount(@RequestBody CustomerDTO customerdto) {

        try {
            return ResponseEntity.ok().body(paymentService.createSubscriber(customerdto));


        } catch (Exception e) {
            log.error("Could not create account for  " + customerdto.getEmail(), e);
            return ResponseEntity.badRequest().body(null);
        }
    }
}
