package core.nxg.utils;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;

import java.security.NoSuchAlgorithmException;
import java.time.LocalTime;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import static core.nxg.utils.constants.EmailConstant.HEADER_SIGNATURE_DELIVERY_CONTENT;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecretService {

    @Autowired
    private  JavaMailSender mailSender ;

    private final Helper helper;



    @Value("${spring.mail.username}")
    private String FROM_ADDRESS;

    @Value("classpath:images/nxg-logo.png")
    Resource nxgLogo ;

    @Autowired
    private  HeaderSignatureStorage storage;



     //
    public void init()  {

        try{

            storage.deleteAll();

        log.info("Generating new header signatures");

        SecretKey secretKeyONE = generateSecret();

        HeaderSignature sigONE = new HeaderSignature(secretKeyONE);

        SecretKey secretKeyTWO =  generateSecret();

        HeaderSignature sigTWO = new HeaderSignature(secretKeyTWO);

        var secretKeyTHREE = generateSecret();

        HeaderSignature sigTHREE = new HeaderSignature(secretKeyTHREE);

        storage.saveAll(List.of( sigONE, sigTWO,sigTHREE));

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

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setFrom(FROM_ADDRESS, "NXG JOB HUB ADMIN SERVICE");
        helper.setTo(FROM_ADDRESS);

        helper.setSubject("NEWLY GENERATED HEADER SIGNATURES FOR " + new Date() + "AT" + LocalTime.now());

        String content = HEADER_SIGNATURE_DELIVERY_CONTENT.replace("[[header_signature_ONE]]", keyToString( secretKeyONE));

        content = content.replace( "[[header_signature_TWO]]", keyToString(secretKeyTWO));

        content = content.replace( "[[header_signature_THREE]]",keyToString( secretKeyTHREE));

        helper.setText(content, true);
        helper.addInline("nxgLogo", nxgLogo);

        mailSender.send(mimeMessage);



    }

//    public boolean decodeKeyFromHeaderAndValidate(HttpServletRequest request) {
//
//        var header = request.getHeader("x-nxg-header");
//
//        if (header.isEmpty() || header.isBlank()) {
//            log.error("**via registration. Header Is Empty" );
//            return false;
//        }
//        byte[] decodedKey = Base64.getDecoder().decode(header);
//
//        SecretKey secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");
//
//        return  storage.existsByValueIgnoreCase(Base64.getEncoder().encodeToString(secretKey.getEncoded()));
//
//
//    }
public boolean decodeKeyFromHeaderAndValidate(HttpServletRequest request) {
    String header = request.getHeader("x-nxg-header");

    if (header == null || header.isEmpty() || header.isBlank()) {
        log.error("**via registration. Header Is Empty");
        return false;
    }

    try {
        byte[] decodedKey = Base64.getDecoder().decode(header);
        SecretKey secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");
        String encodedSignature = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        return storage.existsBySignature(encodedSignature);
    } catch (IllegalArgumentException e) {
        log.error("**via registration. Invalid Header Format", e);
        return false;
    }
}

}
