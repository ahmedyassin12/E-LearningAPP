package com.example.demo.auth.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    private static final String EMAIL_TEMPLATE = """
        <!DOCTYPE html>
        <html>
        <head>
            <style>
                .button {
                    background-color: #4CAF50;
                    border: none;
                    color: white;
                    padding: 15px 32px;
                    text-align: center;
                    text-decoration: none;
                    display: inline-block;
                    font-size: 16px;
                    margin: 20px 0;
                    cursor: pointer;
                    border-radius: 5px;
                }
                .container {
                    font-family: Arial, sans-serif;
                    max-width: 600px;
                    margin: 0 auto;
                    padding: 20px;
                    border: 1px solid #ddd;
                    border-radius: 5px;
                }
            </style>
        </head>
        <body>
            <div class="container">
                <h2>%s</h2>
                <p>Hello,</p>
                <p>%s</p>
                <a href="%s" class="button">Verify Email</a>
                <p>Or copy and paste this link into your browser:</p>
                <p><a href="%s">%s</a></p>
                <p>If you didn't request this, please ignore this email.</p>
            </div>
        </body>
        </html>
        """;

    public void sendVerificationEmail(String email, String token) {
        String subject = "Email Verification";
        String path = "/api/v1/verify";
        String message = "Please verify your email address to complete your registration.";
        sendEmail(email, token, subject, path, message);
    }

    public void sendForgotPasswordEmail(String email, String token) {
        String subject = "Password Reset Request";
        String path = "/api/v1/reset-password";
        String message = "Please click the button below to reset your password.";
        sendEmail(email, token, subject, path, message);
    }

    private void sendEmail(String email, String token, String subject, String path, String message) {
        try {
            String actionUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(path)
                    .queryParam("token", token)
                    .toUriString();

            String content = String.format(EMAIL_TEMPLATE,
                    subject, message, actionUrl, actionUrl, actionUrl);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setFrom(from);
            helper.setText(content, true);
            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            System.err.println("Failed to send email to " + email);
            e.printStackTrace();
        }
    }
}