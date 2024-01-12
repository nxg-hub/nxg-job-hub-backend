//package core.nxg.subscription;
//
//
//import core.nxg.subscription.dto.CustomerDTO;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.parameters.RequestBody;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Slf4j
//@Controller
//@RequiredArgsConstructor
//@RequestMapping("/api/v1/subscriptions")
//public class PayemntController {
//
//    @Autowired
////    private final SubscriptionService paymentService;
//
//
//
//    @Operation(summary = "Create a subscription account for a user")
//    @RequestBody(description = "Create a new subscription account for a user with an " +
//            "email address, first name, " +
//            "last name, phone number and metadata *(a key-value pair object type)* ", required = true, content =
//    @Content(mediaType = "application/json",
//            schema = @Schema(implementation = CustomerDTO.class)))
//    @PostMapping("/create-account")
//    public ResponseEntity<String> createAccount(@RequestBody CustomerDTO customerdto) {
//
//        try {
//            paymentService.create(customerdto);
//            return ResponseEntity.ok("Subscriber created successfully");
//
//        } catch (Exception e) {
//            log.error("Could not create account for  " + customerdto.getEmail(), e);
//            return ResponseEntity.badRequest().body("Error while creating customer");
//        }
//    }
//}
