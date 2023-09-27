package com.financeTracker.financeTracker.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.io.UnsupportedEncodingException;

@Service
public class EmailUtils {
    @NonNull
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String sender;

    public void sendRegistrationMail(String firstName,String receiver, String otp) throws  UnsupportedEncodingException, jakarta.mail.MessagingException {
        String content = "Hello! " + firstName +" your verification code is " + otp;
        jakarta.mail.internet.MimeMessage mail = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail, true);
        helper.setTo(receiver);
        helper.setFrom(String.valueOf(new InternetAddress(sender,"Finance Tracking App")));
        helper.setReplyTo("noreply@financeTracker.com");
        helper.setSubject("Please Activate your account");
        helper.setText(content, false);
        helper.setReplyTo("noreply@ubuntu.com");
        mailSender.send(mail);
    }

    public void sendForgotPasswordMail(String firstName,String receiver, String otp) throws MessagingException, UnsupportedEncodingException, jakarta.mail.MessagingException {
        String content = "Hello! " + firstName +" looks like you have forgotten your password, here is an otp create a new password " + otp;

        MimeMessage mail = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail, true);
        helper.setTo(receiver);
        helper.setFrom(String.valueOf(new InternetAddress(sender,"Finance Tracking App")));
        helper.setReplyTo("noreply@financeTracker.com");
        helper.setSubject("Forgot password");
        helper.setText(content, false);
        helper.setReplyTo("noreply@ubuntu.com");
        mailSender.send(mail);
    }
}
