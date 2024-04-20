package org.ntnu.idi.idatt2106.sparesti.sparestibackend.service;

import lombok.RequiredArgsConstructor;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.ChangePasswordRequestRequest;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.BadInputException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.ChangePasswordRequest;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.repository.ChangePasswordRequestRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.UUID;
import java.util.regex.Pattern;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


@Service
@RequiredArgsConstructor
public class ChangePasswordRequestService {

  private final UserService userService;
  private final ChangePasswordRequestRepository changePasswordRequestRepository;
  private final PasswordEncoder passwordEncoder;

  public void sendForgotPasswordEmail(ChangePasswordRequestRequest request) throws MessagingException {
    if (!isEmailValid(request.getEmail())) {
      throw new BadInputException("The email address is invalid.");
    }
    UUID uniqueKey = UUID.randomUUID();
    String encodedUniqueKey = passwordEncoder.encode(uniqueKey.toString());

    save(request.getEmail(), encodedUniqueKey);

    try {
      sendEmail(request.getEmail(), uniqueKey.toString());
    } catch (MessagingException e) {
      throw new MessagingException("Server error");
    }
  }

  private void save(String email, String encodedUniqueKey) {
    if (userService.userExistByEmail(email)) {

      long userID = userService.findUserByEmail(email).getId();
      ChangePasswordRequest cpr = ChangePasswordRequest
        .builder()
        .id(encodedUniqueKey)
        .userID(userID)
        .build();

      if (!changePasswordRequestExistsByUserID(userID)) {
        changePasswordRequestRepository.save(cpr);
      }
    }
  }

  private boolean changePasswordRequestExistsByUserID(Long userID) {
    return changePasswordRequestRepository.findChangePasswordRequestByUserID(userID).isPresent();
  }

  private boolean isEmailValid(String email) {
    String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    return Pattern.compile(emailPattern).matcher(email).matches();
  }

  private void sendEmail(String email, String uniqueKey) throws MessagingException {
    Session session = createEmailSession();

    Message message = new MimeMessage(session);
    message.setFrom(new InternetAddress(email)); // Assuming 'email' is a valid email
    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
    message.setSubject("Reset Password");
    message.setText("http://localhost:5173/" + uniqueKey);

    Transport.send(message);
  }

  private Session createEmailSession() {
    Properties prop = new Properties();
    prop.put("mail.smtp.host", "smtp.gmail.com");
    prop.put("mail.smtp.port", "587");
    prop.put("mail.smtp.auth", "true");
    prop.put("mail.smtp.starttls.enable", "true");

    return Session.getInstance(prop, new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(System.getenv("EMAIL_USERNAME"), System.getenv("EMAIL_PASSWORD"));
      }
    });
  }
}
