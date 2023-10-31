package core.nxg.service;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import core.nxg.entity.OTP;
import core.nxg.repository.OTPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OTPService {

    @Value("${twilio.number}")
    String twilioSender;
    @Value("${twilio.sid}")
    private String twilioAccountSid ;
    //Your Twilio Account SID


    @Value("${twilio.token}")
    private String twilioAuthToken ;//Your Twilio Auth Token

    private final OTPRepository otpRepository;

    @Autowired
    public OTPService(OTPRepository otpRepository) {
        this.otpRepository = otpRepository;
    }

    public OTP generateOTP(String email, String phoneNumber) {
        Random random = new Random();
        String otp = String.format("%04d", random.nextInt(10000)); // 4-digit OTP
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5); // OTP expires in 5 minutes
        OTP otpEntity = new OTP();
        otpEntity.setEmail(email);
        otpEntity.setOtp(otp);
        otpEntity.setExpiryTime(expiryTime);



        // Send OTP via SMS
        Twilio.init(twilioAccountSid, twilioAuthToken);
        String messageBody = "Your OTP is: " + otp;
        Message message = Message.creator(new com.twilio.type.PhoneNumber(phoneNumber), // recipient's phone number
                new com.twilio.type.PhoneNumber(twilioSender), // your Twilio phone number
                messageBody).create();

        return otpRepository.save(otpEntity);
    }

    public boolean validateOTP(String email, String otp) {
        LocalDateTime currentTime = LocalDateTime.now();
        OTP otpEntity = otpRepository.findByEmailAndOtpAndExpiryTimeAfter(email, otp, currentTime);
        return otpEntity != null;
    }
}