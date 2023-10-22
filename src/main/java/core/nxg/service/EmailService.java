package core.nxg.service;


import core.nxg.dto.UserDTO;
import core.nxg.entity.VerificationCode;
import jakarta.mail.MessagingException;
import org.springframework.mail.MailException;

import java.io.UnsupportedEncodingException;


public interface EmailService {

    public void sendVerificationEmail(UserDTO request , VerificationCode code , String siteURL  ) throws MessagingException, UnsupportedEncodingException, MailException ;
//    public void sendPasswordResetEmail(UserDTO request , VerificationCode code , String siteURL  ) throws MessagingException, UnsupportedEncodingException, MailException ;
    public void confirmVerificationEmail(String verificationCode) throws Exception;
}