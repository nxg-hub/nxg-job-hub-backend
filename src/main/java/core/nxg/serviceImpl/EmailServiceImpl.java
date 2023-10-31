package core.nxg.serviceImpl;


import core.nxg.dto.EmailDTO;
import core.nxg.dto.UserDTO;
import core.nxg.entity.User;
import core.nxg.entity.VerificationCode;
import core.nxg.exceptions.AccountExpiredException;
import core.nxg.exceptions.TokenExpiredException;
import core.nxg.exceptions.TokenNotFoundException;
import core.nxg.exceptions.UserNotFoundException;
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
public void confirmReset(String verificationCode) throws Exception {

    Optional<VerificationCode> verification = verificationRepo.findByCode(verificationCode);
    if (verification.isEmpty()) {
        throw new TokenNotFoundException("Invalid reset code!");}

    if (verification.get().isExpired()) {
        throw new TokenExpiredException("Verification code has expired!");
    } else {
        userRepository.save(verification.get().getUser());
        verificationRepo.deleteById(verification.get().getId());
    }
}


@Override
public void confirmVerification(String verificationCode) throws  Exception {

    Optional<VerificationCode> verification = verificationRepo.findByCode(verificationCode);
    if (verification.isEmpty()) {
        throw new TokenNotFoundException("Invalid verification code");
    } else {
        if (verification.get().isExpired()) {
            throw new TokenExpiredException("Verification code has expired");
        } else {
            verification.get().getUser().setEnabled(true);
            userRepository.save(verification.get().getUser());
            verificationRepo.deleteById(verification.get().getId());
        }
    }
}
    @Override
    public void sendPasswordResetEmail(EmailDTO request, String siteURL) throws MessagingException, UnsupportedEncodingException, MailException {
        Optional<User> user = userRepository.findByEmail(request.getEmail());
        if(user.isEmpty()){
            throw new UserNotFoundException("User with email does not exist");}
        if (!user.get().isEnabled()){
            throw new AccountExpiredException("Account is yet to be verified!");}

        else{
            VerificationCode verificationCode = new VerificationCode(user.get());


            String subject = "Password Reset";
            String toAddress = request.getEmail();
            String fromAddress = "abayomioluwatimilehinstephen@gmail.com";
            String senderName = "NXG HUB DIGITECH";
            String content = "<html>"
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
                    + "   <h1 style=\"font-weight: bold;\">Reset Your Password!</h1>"
                    + "   <p>Your request to <strong>Reset your password!</strong> To get started, please click the button.</p>"
                    + "   <a href=\"[[URL]]\" style=\"text-decoration: none;\">"
                    + "     <button style=\"background-color: #007BFF; color: #fff; font-weight: bold; padding: 10px 20px; border: none; border-radius: 5px; cursor: pointer; transition: background-color 0.3s;\">Verify Your Email</button>"
                    + "   </a>"
                    + " </div>"
                    + " <p style=\"margin-top: 50px;\">If you did not make this request please report this incident.</p>"
                    + " <p>Thanks,</p>"
                    + " <p style=\"font-weight: bold;\">The NXG-JOB HUB Team</p>"
                    + "</body>"
                    + "</html>";


            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);

            helper.setSubject(subject);

            String full_name = user.get().getFirstName() + " " + user.get().getLastName();

            content = content.replace("[[name]]", full_name);

            String verification = verificationCode.getCode();

            String verifyURL = siteURL + "/api/v1/auth/reset-password?code=" + verification;


            content = content.replace("[[URL]]", verifyURL);

            helper.setText(content, true);

            mailSender.send(message);
            verificationRepo.saveAndFlush(verificationCode);
        }
    }

    @Override
    public void sendVerificationEmail(UserDTO request, VerificationCode code , String siteURL) throws MessagingException, UnsupportedEncodingException, MailException {


    /* TODO: IMPLEMENT A METHOD TO RESEND VERIFICATION EMAIL IF USER DOES NOT RECEIVE IT OR IT'S EXPIRED */
    
        String subject = "Almost there! Please verify your email address.";
        String toAddress = request.getEmail();
        String fromAddress = "abayomioluwatimilehinstephen@gmail.com";
        String senderName = "NXG HUB DIGITECH";
        String content = "<html>"
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

        helper.setSubject(subject);

        String full_name = request.getFirstName() + " " + request.getLastName();

        content = content.replace("[[name]]", full_name);

        String verification = code.getCode();

        String verifyURL = siteURL + "/api/v1/auth/confirm-email?code=" + verification;


        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);
    }




}