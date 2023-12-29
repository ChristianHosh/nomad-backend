package com.nomad.socialspring.security.service;

import com.nomad.socialspring.error.exceptions.BxException;
import com.nomad.socialspring.security.model.VerificationToken;
import com.nomad.socialspring.user.model.User;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MailService {

    @Value("${nomad.mail.key}")
    private String apiKey;

    @Value("${nomad.mail.from}")
    private String fromEmail;

    public void sendVerificationEmail(User user, VerificationToken verificationToken) {
        Mail mail = getMail(user, verificationToken);
        SendGrid sendGrid = new SendGrid(apiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sendGrid.api(request);
        } catch (IOException e) {
            throw BxException.hardcoded(MailService.class, e);
        }
    }

    @NotNull
    @Contract("_,_-> new")
    private Mail getMail(@NotNull User user, @NotNull VerificationToken verificationToken) {
        Email from = new Email(fromEmail);
        Email to = new Email(user.getEmail());

        String verificationLink = "localhost:8080/api/auth/verify_email?token=" + verificationToken.getToken();
        Content content = new Content(
                "text/html",
                "<html>" +
                        "   <body>" +
                        "       <a href='" + verificationLink + "' target='_blank'>" +
                        "           Click here to confirm your email" +
                        "       </a>" +
                        "   </body>" +
                        "</html>"
        );

        return new Mail(from, "Confirm your email", to, content);
    }
}
