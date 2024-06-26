package core.nxg.serviceImpl;


import core.nxg.configs.JwtService;
import core.nxg.entity.JobPosting;
import core.nxg.entity.User;
import core.nxg.entity.VerificationCode;
import core.nxg.exceptions.*;
import core.nxg.repository.TechTalentRepository;
import core.nxg.repository.UserRepository;
import core.nxg.repository.VerificationCodeRepository;
import core.nxg.service.EmailService;
import core.nxg.utils.Helper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.time.LocalTime;
import java.util.Date;

import java.util.Optional;

import static core.nxg.utils.constants.EmailConstant.*;


@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private static final String GENERAL_FROM_ADDRESS = "josgolf3@gmail.com";
    private static final String GENERAL_FROM_NAME = "NXG HUB DIGITECH";

    @Value("${server.url}")
    private String DEFAULT_SERVER_URL ;
    @Autowired
    JavaMailSender mailSender;

    @Autowired
    JwtService jwtService;

    @Value("classpath:images/nxg-logo.png")
    Resource nxgLogo;

    @Autowired
    TechTalentRepository TechTalentRepository;


    @Autowired
    UserRepository userRepository;

    @Autowired
    VerificationCodeRepository verificationRepo;

    @Autowired
    Helper helper;


    @Override
    public void confirmVerification(String verificationCode) throws Exception {

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
    public void sendPasswordResetEmail(String email, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException, MailException, ExpiredJWTException {
        var siteURL = helper.getSiteURL(request);

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            return;
        }
        if (!user.get().isEnabled()) {
            throw new AccountExpiredException("Account is yet to be verified! Kindly request a verification email.");
        } else {
            VerificationCode verificationCode = new VerificationCode(user.get());


            String subject = "Password Reset";
            String toAddress = user.get().getEmail();
            String content = PASSWORD_RESET_CONTENT;


            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom(GENERAL_FROM_ADDRESS, GENERAL_FROM_NAME);
            helper.setTo(toAddress);

            helper.setSubject(subject);

            String full_name = user.get().getFirstName() + " " + user.get().getLastName();

            content = content.replace("[[name]]", full_name);

            String verification = verificationCode.getCode();

            String verifyURL = siteURL + "/api/v1/auth/reset-password?code=" + verification;


            content = content.replace("[[URL]]", verifyURL);

            helper.setText(content, true);
            helper.addInline("nxgLogo", nxgLogo);

            mailSender.send(message);
            verificationRepo.saveAndFlush(verificationCode);
        }
    }

    @Override
    public void sendVerificationEmail(
            VerificationCode code,
            String siteURL
    ) throws MessagingException, IOException, MailException {


        User user = code.getUser();
        String subject = "Almost there! Please verify your email address.";
        String toAddress = user.getEmail();
        String content = VERIFICATION_EMAIL_CONTENT;


        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(GENERAL_FROM_ADDRESS, GENERAL_FROM_NAME);
        helper.setTo(toAddress);


        helper.setSubject(subject);

        String full_name = user.getFirstName() + " " + user.getLastName();

        content = content.replace("[[name]]", full_name);

        String verification = code.getCode();

        String verifyURL = siteURL + "/api/v1/auth/confirm-email?code=" + verification;


        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);
        helper.addInline("nxgLogo", nxgLogo);


        mailSender.send(message);
    }

    @Override
    public void reSendVerificationEmail(
            String email, String siteURL) throws MessagingException, UnsupportedEncodingException, MailException {


        Optional<User> user1 = userRepository.findByEmail(email);
        if (user1.isEmpty()) throw new UserNotFoundException("User cant be found!");


        User user = user1.get();
        if (user.isEnabled()) return; //no email sent if user is already verified


        String subject = "Almost there! Please verify your email address.";
        String toAddress = user.getEmail();
        String content = VERIFICATION_EMAIL_CONTENT;


        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");


        helper.setFrom(GENERAL_FROM_ADDRESS, GENERAL_FROM_NAME);
        helper.setTo(toAddress);

        helper.setSubject(subject);

        String full_name = user.getFirstName() + " " + user.getLastName();

        content = content.replace("[[name]]", full_name);

        VerificationCode code = new VerificationCode(user);

        String verification = code.getCode();

        String verifyURL = siteURL + "/api/v1/auth/confirm-email?code=" + verification;


        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);
        helper.addInline("nxgLogo", nxgLogo);


        verificationRepo.saveAndFlush(code);

        mailSender.send(message);
    }


    @Override
    public void sendJobRelatedNotifEmail(String to, JobPosting job) throws MailException, UnsupportedEncodingException, MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        String firstName = userRepository.findByEmail(to).get().getFirstName();
        String content = JOBPOSTING_NOTIFICATION_CONTENT.replace("[[name]]", firstName);

        content = content.replace("[[job_title]]", job.getJob_title());
        content = content.replace("[[job_description]]", job.getJob_description());
        content = content.replace("[[job_location]]", job.getJob_location());
        content = content.replace("[[company_bio]]", job.getCompany_bio());
        content = content.replace("[[jobURL]]", DEFAULT_SERVER_URL+ "/job/"+ job.getJobID() );


        helper.setFrom(GENERAL_FROM_ADDRESS, GENERAL_FROM_NAME + "Job Posting Notification");
        helper.setTo(to);
        helper.setSubject("New job posted: " + job.getJob_title());

        helper.setText(content, true);
        helper.addInline("nxgLogo", nxgLogo);

        mailSender.send(message);


    }

    @Override
    public void sendOAuthUserLoginDetails(String name, String email, String generatedPassword) throws MessagingException, UnsupportedEncodingException, MailException, ExpiredJWTException {

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User with email does not exist");
        }
        if (!user.get().isEnabled()) {
            throw new AccountExpiredException("Account is yet to be verified! Kindly request a verification email.");
        } else {


            String mailSubject = "NXG JOB HUB LOGIN DETAILS";
            String mailto = user.get().getEmail();
            String content = OAUTH_MAIL_CONTENT
                    .replace("[[name]]", name)
                    .replace("[[email]]", email)
                    .replace("[[password]]", generatedPassword);


            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(GENERAL_FROM_ADDRESS, GENERAL_FROM_NAME);
            helper.setTo(mailto);

            helper.setSubject(mailSubject);
            helper.setText(content, true);
            helper.addInline("nxgLogo", nxgLogo);

            mailSender.send(message);

        }
    }


    public void sendEmailAfterApplied(String employerEmail, String applicantEmail) throws MessagingException, UnsupportedEncodingException {


        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        String firstNameEmpl = userRepository.findByEmail(employerEmail).get().getFirstName();
        String firstNameAppl = userRepository.findByEmail(applicantEmail).get().getFirstName();

        String content = JOB_APPLICATION_EMAIL_CONTENT.replace("[[name]]", firstNameEmpl);



        helper.setFrom(GENERAL_FROM_ADDRESS, GENERAL_FROM_NAME + "Application Notification");
        helper.setTo(employerEmail);
        helper.setSubject("You have a new application from " + firstNameAppl);

        helper.setText(content, true);
        helper.addInline("nxgLogo", nxgLogo);

        mailSender.send(message);



}


}

