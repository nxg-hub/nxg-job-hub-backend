package core.nxg.utils;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.time.LocalTime;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import static core.nxg.utils.constants.EmailConstant.HEADER_SIGNATURE_DELIVERY_CONTENT;

@Slf4j
@Service
public class SecretService {

    @Autowired
    private  JavaMailSender mailSender ;



    @Value("${spring.mail.username}")
    private String FROM_ADDRESS;

    @Autowired
    private HeaderSignatureStorage storage;



    @Scheduled(fixedRate = 300000) //
    public void init()  {

        try{

        log.info("Generating new header signatures");

        var secretKeyONE = generateSecret();

        HeaderSignature sigONE = new HeaderSignature(secretKeyONE);

        var secretKeyTWO = generateSecret();

        HeaderSignature sigTWO = new HeaderSignature(secretKeyTWO);

        var secretKeyTHREE = generateSecret();

        HeaderSignature sigTHREE = new HeaderSignature(secretKeyTHREE);

        storage.saveAllAndFlush(List.of( sigONE, sigTWO,sigTHREE));

        log.info("New header signatures generated");



        deliverHeaderSignatures(secretKeyONE, secretKeyTWO, secretKeyTHREE);

        log.info("Header signatures delivered");



    } catch (MessagingException | NoSuchAlgorithmException| UnsupportedEncodingException e)
        {
            log.error("Error while generating header signatures: {}", e.getMessage());
    }
    }


    private SecretKey generateSecret() throws NoSuchAlgorithmException {

        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");

        return keyGen.generateKey();


    }

    private String keyToString(SecretKey key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }





    public void deliverHeaderSignatures(
            SecretKey secretKeyONE,
    SecretKey secretKeyTWO,
   SecretKey secretKeyTHREE) throws MessagingException, UnsupportedEncodingException {



        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

        helper.setFrom(FROM_ADDRESS, "NXG JOB HUB ADMIN SERVICE");
        helper.setTo(FROM_ADDRESS);

        helper.setSubject("NEWLY GENERATED HEADER SIGNATURES FOR " + new Date() + "AT" + LocalTime.now());

        String content = HEADER_SIGNATURE_DELIVERY_CONTENT.replace("[[header_signature_ONE]]", keyToString( secretKeyONE));

        content = content.replace( "[[header_signature_TWO]]", keyToString(secretKeyTWO));

        content = content.replace( "[[header_signature_THREE]]",keyToString(  secretKeyTHREE));

        helper.setText(content, true);

        mailSender.send(mimeMessage);



    }

    public boolean decodeKeyFromHeaderAndValidate(String header) {
        byte[] decodedKey = Base64.getDecoder().decode(header);

        SecretKey secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");

        return  storage.existsByValueIgnoreCase(Base64.getEncoder().encodeToString(secretKey.getEncoded()));


    }

}
