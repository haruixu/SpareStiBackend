package org.ntnu.idi.idatt2106.sparesti.sparestibackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user.ChangePasswordRequestRequest;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user.ResetPasswordRequest;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.validation.BadInputException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.validation.ObjectNotValidException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.service.ChangePasswordRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing forgotPassword endpoints
 *
 * @author Lars N.
 * @version 1.0
 * @since 20.4.24
 */
@RestController
@CrossOrigin
@RequestMapping("/forgotPassword")
@RequiredArgsConstructor
@Tag(
        name = "Change password endpoints",
        description = "Endpoints for requesting password requests and resetting passwords")
public class ChangePasswordController {

    private final ChangePasswordRequestService changePasswordRequestService;

    /**
     * Parses a request for forgot password and sends a mail
     * with a link for resetting password. The response is always the same
     * to prevent user's from finding each other's mails
     * @param changePasswordRequestRequest Wrapper for a user's mail
     * @return OK-string
     * @throws BadInputException For invalid mail
     * @throws MessagingException If mail could not be sent - non-existent mail
     * @throws ObjectNotValidException If mail is invalid
     */
    @Operation(
            summary = "Sends a mail with new password",
            description = "Sends a mail with link for resetting password")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Sent mail", content = @Content)
            })
    @PostMapping("/changePasswordRequest")
    public ResponseEntity<String> changePasswordRequest(
            @RequestBody ChangePasswordRequestRequest changePasswordRequestRequest)
            throws BadInputException, MessagingException, ObjectNotValidException {
        changePasswordRequestService.sendForgotPasswordEmail(changePasswordRequestRequest);
        return ResponseEntity.ok("OK");
    }

    /**
     * Resets a user's password
     * @param resetPasswordRequest Wrapper for info for resetting password
     * @return OK-string
     * @throws BadInputException For bad user input
     * @throws ObjectNotValidException If input data fields are invalid
     */
    @Operation(summary = "Rest password", description = "Resets a user's password")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Reset password",
                        content = @Content)
            })
    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(
            @RequestBody ResetPasswordRequest resetPasswordRequest)
            throws BadInputException, ObjectNotValidException {
        changePasswordRequestService.resetPassword(resetPasswordRequest);
        return ResponseEntity.ok("OK");
    }
}
