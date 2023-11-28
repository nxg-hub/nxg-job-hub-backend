package core.nxg.serviceImpl;


import core.nxg.dto.EmailDTO;
import core.nxg.dto.TechTalentDTO;
import core.nxg.entity.JobPosting;
import core.nxg.entity.TechTalentUser;
import core.nxg.entity.User;
import core.nxg.entity.VerificationCode;
import core.nxg.exceptions.AccountExpiredException;
import core.nxg.exceptions.TokenExpiredException;
import core.nxg.exceptions.TokenNotFoundException;
import core.nxg.exceptions.UserNotFoundException;
import core.nxg.repository.TechTalentRepository;
import core.nxg.repository.UserRepository;
import core.nxg.repository.VerificationCodeRepository;
import core.nxg.service.EmailService;
import core.nxg.utils.Helper;
import static core.nxg.utils.constants.EmailConstant.PASSWORD_RESET_CONTENT;
import static core.nxg.utils.constants.EmailConstant.VERIFICATION_EMAIL_CONTENT;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchProperties.Job;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Autowired
    JavaMailSender mailSender;


    @Autowired
    TechTalentRepository TechTalentRepository;

    

    @Autowired
    UserRepository userRepository;

    @Autowired
    VerificationCodeRepository verificationRepo;

    @Autowired
    Helper helper;




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
                verificationRepo.deleteById(verification.get().getId());
                throw new TokenExpiredException("Verification code has expired");
            } else {
                verification.get().getUser().setEnabled(true);
                userRepository.save(verification.get().getUser());
                verificationRepo.deleteById(verification.get().getId());
            }
        }
    }
    @Override
    public void sendPasswordResetEmail(EmailDTO dto, String siteURL, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException, MailException {
        User loggedInUser = helper.extractLoggedInUser(request);
        Optional<User> user = userRepository.findByEmail(loggedInUser.getEmail());
        if(user.isEmpty()){
            throw new UserNotFoundException("User with email does not exist");}
        if (!user.get().isEnabled()){
            throw new AccountExpiredException("Account is yet to be verified!");}

        else{
            VerificationCode verificationCode = new VerificationCode(user.get());


            String subject = "Password Reset";
            String toAddress = loggedInUser.getEmail();
            String fromAddress = "josgolf3@@gmail.com";
            String senderName = "NXG HUB DIGITECH";
            String content  = PASSWORD_RESET_CONTENT;


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
    public void sendVerificationEmail(
    VerificationCode code , 
    String siteURL
   ) throws MessagingException, UnsupportedEncodingException, MailException {


        User user = code.getUser();
        String subject = "Almost there! Please verify your email address.";
        String toAddress = user.getEmail();
        String fromAddress = "josgolf3@gmail.com";
        String senderName = "NXG HUB DIGITECH";
        String content = VERIFICATION_EMAIL_CONTENT;


        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);

        helper.setSubject(subject);

        String full_name = user.getFirstName() + " " + user.getLastName();

        content = content.replace("[[name]]", full_name);

        String verification = code.getCode();

        String verifyURL = siteURL + "/api/v1/auth/confirm-email?code=" + verification;


        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);
    }

    @Override
    public void reSendVerificationEmail(
            String email, String siteURL) throws MessagingException, UnsupportedEncodingException, MailException {


        Optional<User> user1 = userRepository.findByEmail(email);
        if (user1.isEmpty()) throw new UserNotFoundException("User cant be found!");



        User user = user1.get();
        if (user.isEnabled()) return;


        String subject = "Almost there! Please verify your email address.";
        String toAddress = user.getEmail();
        String fromAddress = "josgolf3@gmail.com";
        String senderName = "NXG HUB DIGITECH";
        String content = VERIFICATION_EMAIL_CONTENT;


        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);

        helper.setSubject(subject);

        String full_name = user.getFirstName() + " " + user.getLastName();

        content = content.replace("[[name]]", full_name);

        VerificationCode code = new VerificationCode(user);

        String verification = code.getCode();

        String verifyURL = siteURL + "/api/v1/auth/confirm-email?code=" + verification;


        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        verificationRepo.saveAndFlush(code);

        mailSender.send(message);
    }


    @Override
    public void sendJobPostingNotifEmail(String to, JobPosting job) throws MessagingException{
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo(to);
        helper.setSubject("New job posted: " + job.getTitle());
        helper.setText("A new job has been posted that matches your preferences: " + job.getDescription(), true);
        mailSender.send(message);



}
}
