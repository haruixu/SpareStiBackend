package org.ntnu.idi.idatt2106.sparesti.sparestibackend.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.ChangePasswordRequestRequest;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.BadInputException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.service.ChangePasswordRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class ChangePasswordRequestController {

  private final ChangePasswordRequestService changePasswordRequestService;


  @PostMapping("/changePassword")
  public ResponseEntity<String> register(@NonNull @RequestBody
    ChangePasswordRequestRequest changePasswordRequestRequest)
    throws BadInputException, MessagingException {
    changePasswordRequestService.sendForgotPasswordEmail(changePasswordRequestRequest);
    return ResponseEntity.ok("OK");
  }
}
