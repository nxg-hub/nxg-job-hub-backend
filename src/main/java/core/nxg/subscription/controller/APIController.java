package core.nxg.subscription.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.nxg.subscription.enums.EventType;
import core.nxg.subscription.enums.SubscriptionStatus;
import core.nxg.subscription.repository.SubscriptionRepository;
import jakarta.xml.bind.DatatypeConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/callbacks")
public class APIController {



    private final SubscriptionRepository repo;

    @Value("${paystack.secret.active}")
    private static String secretKey;


    @PostMapping("/event")
    public ResponseEntity<Void> event(@RequestBody Map<String, Object> payload, @RequestHeader("x-paystack-signature") String headerSignature) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException,  Exception {



        ObjectMapper mapper = new ObjectMapper();



        TypeReference<JsonNode> typeReference = new TypeReference<>() {
        };
        JsonNode data = mapper.convertValue(payload, typeReference);

        try {
            if (paystackIsValidated(headerSignature, data)) {


                if (payload.get("event").equals(EventType.SUBSCRIPTION_CREATE.getEvent())) {



                    String email = data.get("customer").get("email").asText();

                    repo.findByEmail(email).ifPresent(subscriber -> {
                                subscriber.setSubscriptionStatus(SubscriptionStatus.ACTIVE);
                                repo.save(subscriber);

                            }
                    )
                    ;
                }
                return ResponseEntity.ok().build();
            }
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            log.warn("Error while processing event from  Payload: {}, Header Signature: {}", payload, headerSignature, e);
        }
        return ResponseEntity.badRequest().build();

    }


    public boolean paystackIsValidated(
            String headerSignature,
            JsonNode payload)

            throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, Exception {


        if (headerSignature == null || headerSignature.isEmpty()) {
            throw new Exception("x-paystack-signature header is missing");
        }


        byte[] byteKey = secretKey.getBytes(StandardCharsets.UTF_8);

        String HMAC_SHA512 = "HmacSHA512";
        SecretKeySpec keySpec = new SecretKeySpec(byteKey, HMAC_SHA512);

        Mac sha512_HMAC = Mac.getInstance(HMAC_SHA512);

        sha512_HMAC.init(keySpec);

        byte[] mac_data = sha512_HMAC.

                doFinal(payload.toString().getBytes(StandardCharsets.UTF_8));

        String result = DatatypeConverter.printHexBinary(mac_data);

        return result.toLowerCase().equals(headerSignature);
    }
}












