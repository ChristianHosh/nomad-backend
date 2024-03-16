package com.nomad.socialspring.security;

import com.nomad.socialspring.error.BxException;
import com.nomad.socialspring.image.ImageMapper;
import com.nomad.socialspring.user.User;
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
      if (true)
        return;
      request.setMethod(Method.POST);
      request.setEndpoint("mail/send");
      request.setBody(mail.build());
      sendGrid.api(request);
    } catch (IOException e) {
      throw BxException.unexpected(e);
    }
  }

  @NotNull
  @Contract("_,_-> new")
  @SuppressWarnings("All")
  private Mail getMail(@NotNull User user, @NotNull VerificationToken verificationToken) {
    Email from = new Email(fromEmail);
    Email to = new Email(user.getEmail());

    String verificationLink = ImageMapper.HOSTNAME + "/api/auth/verify-email?token=" + verificationToken.getToken();
    Content content = new Content(
            "text/html",
            //language=HTML
            String.format("""
                    <body>
                        <a href='%s' target='_blank'>
                            Click here to verify your account
                        </a>
                        <p>
                            or go to the following link if above doesn't work
                        </p>
                        <p>
                            %s
                        </p>
                    </body>
                    """, verificationLink, verificationLink)
    );

    return new Mail(from, "Confirm your email", to, content);
  }
}
