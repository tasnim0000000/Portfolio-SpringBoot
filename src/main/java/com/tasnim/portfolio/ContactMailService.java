package com.tasnim.portfolio;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Sends every contact-form submission straight to the portfolio owner's inbox.
 * The visitor's email is set as the Reply-To header, so replying to the
 * notification email replies directly to the person who submitted the form.
 */
@Service
public class ContactMailService {

    private final JavaMailSender mailSender;

    @Value("${portfolio.contact.recipient}")
    private String recipientEmail;

    public ContactMailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendContactEmail(String name, String fromEmail, String message) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(recipientEmail);
        mail.setReplyTo(fromEmail);
        mail.setSubject("Portfolio contact form: " + name);
        mail.setText(
            "You've got a new message from your portfolio site.\n\n"
                + "Name: " + name + "\n"
                + "Email: " + fromEmail + "\n\n"
                + "Message:\n" + message
        );
        mailSender.send(mail);
    }
}
