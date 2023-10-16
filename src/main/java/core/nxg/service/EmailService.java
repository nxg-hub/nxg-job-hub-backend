package core.nxg.service;


import java.io.UnsupportedEncodingException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import core.nxg.dto.UserDTO;
import core.nxg.entity.User;
import core.nxg.entity.VerificationCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import core.nxg.repository.UserRepository;
import core.nxg.repository.VerificationCodeRepository;


@Service
@RequiredArgsConstructor
public class EmailService {

    @Autowired
    private final JavaMailSender javaMailSender;


    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final VerificationCodeRepository verificationRepo;
    public void sendVerificationEmail(UserDTO request ,String siteURL  )throws MessagingException, UnsupportedEncodingException {
       
        Optional<User> userOptional = userRepository.findByEmail(request.getUsername());
        if (userOptional.isEmpty()) {
            return;
        }
        VerificationCode user = verificationRepo.findByUser(userOptional.get());
        if (user == null) {
            return;
        }

        


        String subject = "Your account has been created! | Please verify your email";
        String toAddress = userOptional.get().getEmail();
        String fromAddress = "abayomioluwatimilehinstephen@gmail.com";
        String senderName = "NXG HUB DIGITECH";
        String content =  "<html>" +
        "<br> Dear [[name]],<br>" +
        "<head>" +
        "<style>" +
        "body {" +
        "  font-family: Arial, Helvetica, sans-serif;" +
        "}" +
        "</style>" +
        "</head>" +
        "<body style=\"text-align: center;\">" +
        " <div style=\"margin: 0 auto; width: 50%;\">" +
        "   <h1 style=\"font-weight: bold;\">Verify your email address</h1>" +
        "   <p>Welcome to <strong>NXG-JOB HUB</strong>! To get started, please click the button below to verify your email address.</p>" +
        "   <a href=\"[[URL]]?method=get\" style=\"text-decoration: none;\">" +
        "     <button style=\"background-color: #007BFF; color: #fff; font-weight: bold; padding: 10px 20px; border: none; border-radius: 5px; cursor: pointer; transition: background-color 0.3s;\">Verify Your Email</button>" +
        "   </a>" +
        " </div>" +
        "</body>" +
        "</html>";
        
        // "<div class=\"container\">\n" +
        // "<br> Dear [[name]],<br>" +
        // "  <div class=\"row justify-content-center\">\n" +
        // "    <div class=\"col-md-6\">\n" +
        // "      <h1 class=\"text-center\">Verify your email address</h1>\n" +
        // "      <p class=\"text-center\">Welcome to [Company Name]! To get started, please click the button below to verify your email address.</p>\n" +
        // "      <a href=\"[[URL]]?method=get\" class=\"btn btn-primary btn-lg\">Verify Your Email</a>\n" +
        // "      <br> Sincerely,</br>" + 
        // "       NXG JOB HUB.<br>" +
        // "    </div>\n" +
        // "  </div>\n" +
        // "</div>";
        
              
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        
        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        String full_name = userOptional.get().getFirstName() + " " + userOptional.get().getLastName();
        content = content.replace("[[name]]", full_name);
        String verifyURL = siteURL + "/api/v1/auth/confirm-email?code=" + user.getCode();
        
        content = content.replace("[[URL]]", verifyURL);
        
        helper.setText(content, true);
        
        javaMailSender.send(message);

    }
    
}
