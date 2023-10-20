package core.nxg.serviceImpl;


import core.nxg.dto.EmailDTO;
import core.nxg.entity.User;
import core.nxg.entity.VerificationCode;
import core.nxg.repository.UserRepository;
import core.nxg.repository.VerificationCodeRepository;
import core.nxg.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Autowired
    JavaMailSender mailSender;


    @Autowired
    UserRepository userRepository;

    @Autowired
    VerificationCodeRepository verificationRepo;

    @Override
    public void sendVerificationEmail(EmailDTO request, String siteURL) throws MessagingException, UnsupportedEncodingException, MailException {

        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isPresent()) {
//
            VerificationCode user = verificationRepo.findByUser(userOptional.get());

            if (user != null) {

                String subject = "[[message]]";
                String toAddress = userOptional.get().getEmail();
                //                String toAddress = request.getEmail();
                String fromAddress = "fromAddress";
                String senderName = "NXG HUB DIGITECH";
                String content =


                        "<html>"
                                + "<br> Dear [[name]],<br>"
                                + "<head>"
                                + "<style>"
                                + "body {"
                                + "  font-family: Arial, Helvetica, sans-serif;"
                                + "  font-size: 1rem;"
                                + "  line-height: 1.6;"
                                + "  color: #000;"
                                + "}"
                                + "</style>"
                                + "</head>"
                                + "<body style=\"text-align: center;\">"
                                + " <div style=\"margin: 0 auto; width: 50%;\">"
                                + "   <h1 style=\"font-weight: bold;\">Verify your email address</h1>"
                                + "   <p>Welcome to <strong>NXG-JOB HUB</strong>! To get started, please click the button below to verify your email address.</p>"
                                + "   <a href=\"[[URL]]\" style=\"text-decoration: none;\">"
                                + "     <button style=\"background-color: #007BFF; color: #fff; font-weight: bold; padding: 10px 20px; border: none; border-radius: 5px; cursor: pointer; transition: background-color 0.3s;\">Verify Your Email</button>"
                                + "   </a>"
                                + " </div>"
                                + " <p style=\"margin-top: 50px;\">If you did not create an account using this address, please ignore this email.</p>"
                                + " <p>Thanks,</p>"
                                + " <p style=\"font-weight: bold;\">The NXG-JOB HUB Team</p>"
                                + "</body>"
                                + "</html>";


                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message);

                helper.setFrom(fromAddress, senderName);
                helper.setTo(toAddress);

//            content = content.replace("[[message]]", request.getMessage());
                subject = subject.replace("[[message]]", request.getMessage());
                helper.setSubject(subject);
                String full_name = userOptional.get().getFirstName() + " " + userOptional.get().getLastName();
                content = content.replace("[[name]]", full_name);
//                            String verifyURL = siteURL + "/api/v1/auth/confirm-email?code=" + user.get().getCode();
                String verifyURL = siteURL + "/api/v1/auth/confirm-email?code=" + user.getCode();


                content = content.replace("[[URL]]", verifyURL);

                helper.setText(content, true);

                mailSender.send(message);
            }
        }
    }
}
