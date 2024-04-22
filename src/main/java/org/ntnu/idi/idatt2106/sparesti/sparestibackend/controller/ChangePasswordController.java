package org.ntnu.idi.idatt2106.sparesti.sparestibackend.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.ChangePasswordRequestRequest;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.ResetPasswordRequest;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.BadInputException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.service.ChangePasswordRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@RestController
@CrossOrigin
@RequestMapping("/forgotPassword")
@RequiredArgsConstructor
public class ChangePasswordController {

  private final ChangePasswordRequestService changePasswordRequestService;


  @PostMapping("/changePasswordRequest")
  public ResponseEntity<String> changePasswordRequest(@NonNull @RequestBody
                                                        ChangePasswordRequestRequest changePasswordRequestRequest)
    throws BadInputException, MessagingException {
    changePasswordRequestService.sendForgotPasswordEmail(changePasswordRequestRequest);
    return ResponseEntity.ok("OK");
  }

  @PostMapping("/resetPassword")
  public ResponseEntity<String> resetPassword(@NonNull @RequestBody
                                                ResetPasswordRequest resetPasswordRequest)
    throws BadInputException, MessagingException {
    changePasswordRequestService.resetPassword(resetPasswordRequest);
    return ResponseEntity.ok("OK");
  }
}
