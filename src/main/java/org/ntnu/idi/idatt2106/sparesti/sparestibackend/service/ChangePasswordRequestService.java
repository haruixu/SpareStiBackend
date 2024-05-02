package org.ntnu.idi.idatt2106.sparesti.sparestibackend.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.UUID;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user.ChangePasswordRequestRequest;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user.ResetPasswordRequest;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.BadInputException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.ChangePasswordRequest;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.repository.ChangePasswordRequestRepository;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.validation.ObjectValidator;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.validation.RegexValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service for managing password change requests and related operations, such as sending password
 * reset emails and updating user passwords. This service validates requests, manages email
 * interactions, and ensures user authentication changes are handled securely.
 *
 * @author L.M.L Nilsen
 */
@Service
@RequiredArgsConstructor
public class ChangePasswordRequestService {

    private final UserService userService;
    private final ChangePasswordRequestRepository changePasswordRequestRepository;
    private final PasswordEncoder passwordEncoder;

    private final ObjectValidator<ChangePasswordRequestRequest>
            changePasswordRequestRequestValidator;
    private final ObjectValidator<ResetPasswordRequest> resetPasswordRequestValidator;

    /**
     * Sends a password reset email to the user if the email provided is valid and exists in the system.
     * This method generates a unique key for password reset, before sending it via email.
     * The method also encodes the unique key and stores it in the database.
     *
     * @param request DTO containing the email address for the password reset request.
     * @throws MessagingException If there is an error while attempting to send the email.
     * @throws BadInputException If the provided email address is not valid.
     */
    public void sendForgotPasswordEmail(ChangePasswordRequestRequest request)
            throws MessagingException {
        changePasswordRequestRequestValidator.validate(request);
        if (!RegexValidator.isEmailValid(request.email())) {
            throw new BadInputException("The email address is invalid.");
        }
        if (!userService.userExistByEmail(request.email())) {
            return;
        }

        UUID uniqueKey = UUID.randomUUID();
        String encodedUniqueKey = passwordEncoder.encode(uniqueKey.toString());

        save(request.email(), encodedUniqueKey);

        try {
            sendEmail(request.email(), uniqueKey.toString());
        } catch (MessagingException e) {
            throw new MessagingException("Server error");
        }
    }

    /**
     * Resets the user's password if the reset request is valid, the user exists, and the request
     * was made within 24 hours of the change request.
     * Also validates the strength of the new password.
     *
     * @param request DTO containing the user ID, reset ID, and new password.
     * @throws BadInputException If the new password does not meet security criteria.
     */
    public void resetPassword(ResetPasswordRequest request) {
        resetPasswordRequestValidator.validate(request);
        if (!changePasswordRequestExistsByUserID(request.userID())) {
            return;
        }
        if (!isLessThan24HoursAgo(
                changePasswordRequestRepository
                        .findChangePasswordRequestByUserID(request.userID())
                        .get()
                        .getTime())) {
            return;
        }
        if (!RegexValidator.isPasswordStrong(request.newPassword())) {
            throw new BadInputException(
                    "Password must be at least 8 characters long, include numbers, upper and lower"
                            + " case letters, and at least one special character");
        }
        if (passwordEncoder.matches(
                request.resetID(),
                changePasswordRequestRepository.findIdByUserId(request.userID()).get())) {
            userService.updatePassword(request.userID(), request.newPassword());
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

    /**
     * Saves a password reset request in the database. If an existing request is found for the user,
     * it is replaced with the new request.
     *
     * @param email The user's email address associated with the password reset request.
     * @param encodedUniqueKey The encoded unique key for the password reset, used to verify the request.
     */
    public void save(String email, String encodedUniqueKey) {
        if (userService.userExistByEmail(email)) {

            Long userID = userService.findUserByEmail(email).getId();
            ChangePasswordRequest cpr =
                    ChangePasswordRequest.builder().id(encodedUniqueKey).userID(userID).build();

            if (changePasswordRequestExistsByUserID(userID)) {
                changePasswordRequestRepository.deleteById(cpr.getUserID());
            }
            changePasswordRequestRepository.save(cpr);
        }
    }

    /**
     * Checks if a password reset request exists in the database for a given user ID.
     *
     * @param userID The user ID to check for existing password reset requests.
     * @return true if a password reset request exists, false otherwise.
     */
    private boolean changePasswordRequestExistsByUserID(Long userID) {
        return changePasswordRequestRepository
                .findChangePasswordRequestByUserID(userID)
                .isPresent();
    }

    /**
     * Sends an email with a password reset link. This method configures and sends an email
     * to the specified address with a unique reset link that the user can use to reset their password.
     *
     * @param email The email address to which the reset link will be sent.
     * @param uniqueKey The unique key that validates the password reset request.
     * @throws MessagingException If there are issues configuring or sending the email.
     */
    private void sendEmail(String email, String uniqueKey) throws MessagingException {
        Session session = createEmailSession();

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(email)); // Assuming 'email' is a valid email
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
        message.setSubject("Reset Password");

        String userID = userService.findUserByEmail(email).getId().toString();
        message.setText(
                "Hei🐷\n\n"
                        + "Trykk på linken for å endre passordet ditt og logg på SpareSti!\n"
                        + "\nhttp://localhost:5173/forgotPassword?resetID="
                        + uniqueKey
                        + "&userID="
                        + userID);

        Transport.send(message);
    }

    /**
     * Creates and configures a mail session using SMTP settings to send emails.
     * This setup includes the SMTP server details and the credentials used for authentication.
     *
     * @return A configured Session object ready for sending emails.
     */
    private Session createEmailSession() {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");

        return Session.getInstance(
                prop,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                System.getenv("EMAIL_USERNAME"), System.getenv("EMAIL_PASSWORD"));
                    }
                });
    }
}
