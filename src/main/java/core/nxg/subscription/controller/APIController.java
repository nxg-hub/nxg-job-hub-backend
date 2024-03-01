package core.nxg.subscription.controller;
import java.io.UnsupportedEncodingException;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;

import java.security.NoSuchAlgorithmException;
import jakarta.xml.bind.DatatypeConverter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;

import org.json.JSONObject;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.nxg.subscription.enums.EventType;
import core.nxg.subscription.enums.SubscriptionStatus;
import core.nxg.subscription.repository.SubscriptionRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.annotation.Payloads;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.List;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/callbacks")
public class APIController {

    @Autowired
    private SubscriptionRepository repo;

    @Autowired
    @Value("${paystack.secret.active}")
    private final String secretKey;


    @Autowired
    private static final String HMAC_SHA512 = "HmacSHA512";


    @PostMapping("/event")
    public ResponseEntity<Void >event(@RequestBody Map<String, Object> query, @RequestHeader ("x-paystack-signature") String headerSignature ) throws Exception {



        try {
            if (paystackIsValidated( headerSignature, query)) {


                if (query.get("event").equals(EventType.SUBSCRIPTION_CREATE.getEvent())) {

                    ObjectMapper mapper = new ObjectMapper();
                    TypeReference<JsonNode> typeReference = new TypeReference<>() {
                    };
                    JsonNode data = mapper.convertValue(query.get("data"), typeReference);

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
        } catch (Exception e) {
            log.error("Error while processing event from  Query: {}, Header Signature: {}", query, headerSignature, e);
        }
        return ResponseEntity.badRequest().build();

    }








        public boolean paystackIsValidated(
                                 String headerSignature,
                                 Map<String, Object> payload)

                throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, JSONException, Exception{



            if (headerSignature == null || headerSignature.isEmpty()) {
                throw new Exception("x-paystack-signature header is missing");
            }


            JSONObject body = new JSONObject(payload);






            byte [] byteKey = secretKey.getBytes(StandardCharsets.UTF_8);

            SecretKeySpec keySpec = new SecretKeySpec(byteKey, HMAC_SHA512);

            Mac sha512_HMAC = Mac.getInstance(HMAC_SHA512);

            sha512_HMAC.init(keySpec);

            byte [] mac_data = sha512_HMAC.

                    doFinal(body.toString().getBytes(StandardCharsets.UTF_8));

            String result = DatatypeConverter.printHexBinary(mac_data);

            if(result.toLowerCase().equals(headerSignature)) {
                ResponseEntity.ok().build();

                return true;


            }else{
                    ResponseEntity.badRequest().build();
                    throw new InvalidKeyException("Invalid signature");
            }

        }

    }






