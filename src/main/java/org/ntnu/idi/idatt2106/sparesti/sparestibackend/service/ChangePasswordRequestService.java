package org.ntnu.idi.idatt2106.sparesti.sparestibackend.service;

import lombok.RequiredArgsConstructor;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.ChangePasswordRequestRequest;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.ResetPasswordRequest;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.BadInputException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.ChangePasswordRequest;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.repository.ChangePasswordRequestRepository;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
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
  private final UserRepository userRepository;

  public void sendForgotPasswordEmail(ChangePasswordRequestRequest request) throws MessagingException {
    if (!isEmailValid(request.getEmail())) {
      throw new BadInputException("The email address is invalid.");
    }
    if (!userService.userExistByEmail(request.getEmail())) {
      return;
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

  public void resetPassword(ResetPasswordRequest request) {
    if (!changePasswordRequestExistsByUserID(request.getUserID())) {
      return;
    }
    if (!isLessThan24HoursAgo(changePasswordRequestRepository.findChangePasswordRequestByUserID(request.getUserID()).get().getTime())) {
      return;
    }
    if (!isPasswordStrong(request.getNewPassword())) {
      throw new BadInputException(
        "Password must be at least 8 characters long, include numbers, upper and lower"
          + " case letters, and at least one special character");
    }
    if (passwordEncoder.matches(request.getResetID(), changePasswordRequestRepository.findIdByUserId(request.getUserID()).get())) {
      userService.updatePassword(request.getUserID(), request.getNewPassword());
    }
  }

  /**
   * Checked if the given LocalDateTime is less than 24 hours ago.
   *
   * @param dateTime The LocalDateTime to check.
   * @return true if the dateTime is less than 24 hours ago, false otherwise.
   */
  public static boolean isLessThan24HoursAgo(LocalDateTime dateTime) {
    LocalDateTime now = LocalDateTime.now();
    Duration duration = Duration.between(dateTime, now);

    return duration.toHours() < 24 && !duration.isNegative();
  }

  public void save(String email, String encodedUniqueKey) {
    if (userService.userExistByEmail(email)) {

      Long userID = userService.findUserByEmail(email).getId();
      ChangePasswordRequest cpr = ChangePasswordRequest
        .builder()
        .id(encodedUniqueKey)
        .userID(userID)
        .build();

      if (changePasswordRequestExistsByUserID(userID)) {
        changePasswordRequestRepository.deleteById(cpr.getUserID());
      }
      changePasswordRequestRepository.save(cpr);
    }
  }

  /**
   * Checks if a password meets the strength criteria.
   *
   * @param password
   *            The password to check
   * @return true if the password meets the criteria, false otherwise
   */
  private boolean isPasswordStrong(String password) {
    // Example criteria: at least 8 characters, including numbers, letters and at least one
    // special character
    String passwordPattern =
      "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
    return Pattern.compile(passwordPattern).matcher(password).matches();
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

    String userID = userService.findUserByEmail(email).getId().toString();
    message.setText("http://localhost:5173/forgotPassword?resetID=" + uniqueKey + "&userID=" + userID);

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
