package core.nxg.service;


import core.nxg.dto.EmailDTO;
import jakarta.mail.MessagingException;
import java.io.UnsupportedEncodingException;

import org.springframework.mail.MailException;


public interface EmailService {

    public void sendVerificationEmail(EmailDTO request ,String siteURL  ) throws MessagingException, UnsupportedEncodingException, MailException ;
       
    
}