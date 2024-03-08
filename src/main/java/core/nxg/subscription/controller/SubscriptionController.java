package core.nxg.subscription.controller;



import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.nxg.subscription.dto.CustomerDTO;
import core.nxg.subscription.dto.SubscribeDTO;
import core.nxg.subscription.dto.VerificationDTO;
import core.nxg.subscription.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    @Autowired
    private final SubscriptionService paymentService;


    @Operation(summary = "Create a subscription account for a user")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Create a new subscription account for a user with an " +
            "email address, planType ", required = true, content =
    @Content(mediaType = "application/json",
            schema = @Schema(implementation = CustomerDTO.class)))
    @PostMapping("/create-account")
    public ResponseEntity<JsonNode> createAccount(@Validated @RequestBody Map<String, Object> request) {

        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.createSubscriber(request));


        } catch (Exception e) {
            log.error("Could not create account for  " + request.get("email").toString(), e);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode err = mapper.convertValue(e.getMessage(), JsonNode.class);
            return ResponseEntity.badRequest().body(err);
        }
    }





    @Operation(summary = "Subscribe a user to a plan. Based on the plan type, the user will be charged accordingly")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Subscribe a user with " +
            "email address,  and a callback_url", required = true, content =
    @Content(mediaType = "application/json",
            schema = @Schema(implementation = SubscribeDTO.class)))
    @PostMapping("/subscribe")
    public ResponseEntity<JsonNode> subscribe(@RequestBody SubscribeDTO dto) throws Exception {

        try {
            JsonNode response = paymentService.subscribe(dto);
            /* make provision to change the plan type */
            /* from the current to a new plan type */

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            log.error("Could not initialize subscription for  " + dto.getEmail(), e);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode error = mapper.convertValue(e.getMessage(), JsonNode.class);

            return ResponseEntity.badRequest().body(error);
        }


    }

    @Operation(summary = "Validate  a customer before a transfer or virtual account creation")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Validate a customer with a bank-account", required = true, content =
    @Content(mediaType = "application/json",
            schema = @Schema(implementation = VerificationDTO.class)))
    @PostMapping("/verify-customer")
    public ResponseEntity<JsonNode> verifyCustomer(@RequestBody VerificationDTO dto) throws Exception {

        try {
            JsonNode response = paymentService.validateCustomer(dto);

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode error = mapper.convertValue(e.getMessage(), JsonNode.class);

            return ResponseEntity.badRequest().body(error);
        }

    }
}
