package core.nxg.service;


import core.nxg.dto.EmailDTO;
import core.nxg.dto.UserDTO;
import core.nxg.entity.VerificationCode;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.mail.MailException;
//import jakarta.mail.MessagingException;
//import org.springframework.mail.MailException;

import java.io.UnsupportedEncodingException;


public interface EmailService {

    public void sendVerificationEmail( VerificationCode code , String siteURL, HttpServletRequest request  ) throws MessagingException, UnsupportedEncodingException, MailException;
    public void sendPasswordResetEmail(EmailDTO dto ,String siteURL , HttpServletRequest request ) throws MessagingException, UnsupportedEncodingException, MailException ;
    public void confirmVerification(String verificationCode) throws Exception;

    public void confirmReset(String verificationCode) throws Exception;
}