package com.cosmicbook.search_and_rescue_droid.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String toEmail, String username, String otp) {
        String subject = "Verify your Email - Autonomous Search and Rescue";

        String content = """
            <html>
            <head>
                <style>
                    .email-container {
                        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                        background-color: #f5f5f5;
                        padding: 40px 20px;
                    }
                    .email-content {
                        max-width: 600px;
                        margin: auto;
                        background-color: #ffffff;
                        border-radius: 8px;
                        overflow: hidden;
                        box-shadow: 0 0 10px rgba(0,0,0,0.15);
                    }
                    .header {
                        background-color: #b30000;
                        color: white;
                        padding: 20px;
                        text-align: center;
                        font-size: 20px;
                        font-weight: bold;
                    }
                    .body {
                        padding: 30px 25px;
                        color: #333;
                    }
                    .otp {
                        font-size: 24px;
                        font-weight: bold;
                        color: #b30000;
                        margin: 20px 0;
                    }
                    .footer {
                        background-color: #222;
                        color: #ccc;
                        padding: 15px;
                        text-align: center;
                        font-size: 14px;
                    }
                    a {
                        color: #ff4d4d;
                        text-decoration: none;
                    }
                </style>
            </head>
            <body>
                <div class="email-container">
                    <div class="email-content">
                        <div class="header">Verify Your Email</div>
                        <div class="body">
                            <p>Hi, <strong>%s</strong> 👋</p>
                            <p>We received a signup request for your email, but it hasn’t been verified yet.</p>
                            <p>Please use the OTP below to verify your email (valid for 10 minutes):</p>
                            <div class="otp">%s</div>
                            <p>If you did not make this request, feel free to ignore this email.</p>
                        </div>
                        <div class="footer">
                            Autonomous Search and Rescue<br>
                            <a href="https://www.google.com">Visit our website</a>
                        </div>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(username, otp);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(content, true); // true = HTML
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
