package org.ntnu.idi.idatt2106.sparesti.sparestibackend.controller;

import javax.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user.ChangePasswordRequestRequest;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user.ResetPasswordRequest;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.BadInputException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.validation.ObjectNotValidException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.service.ChangePasswordRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/forgotPassword")
@RequiredArgsConstructor
public class ChangePasswordController {

    private final ChangePasswordRequestService changePasswordRequestService;

    @PostMapping("/changePasswordRequest")
    public ResponseEntity<String> changePasswordRequest(
            @RequestBody ChangePasswordRequestRequest changePasswordRequestRequest)
            throws BadInputException, MessagingException, ObjectNotValidException {
        changePasswordRequestService.sendForgotPasswordEmail(changePasswordRequestRequest);
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(
            @RequestBody ResetPasswordRequest resetPasswordRequest)
            throws BadInputException, ObjectNotValidException {
        changePasswordRequestService.resetPassword(resetPasswordRequest);
        return ResponseEntity.ok("OK");
    }
}
