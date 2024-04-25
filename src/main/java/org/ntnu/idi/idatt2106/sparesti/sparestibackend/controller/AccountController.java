package org.ntnu.idi.idatt2106.sparesti.sparestibackend.controller;

import lombok.RequiredArgsConstructor;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.account.AccountDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.account.AccountResponseDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.account.AccountUpdateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.validation.ObjectNotValidException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.service.AccountService;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/me/accounts")
@RequiredArgsConstructor
@CrossOrigin
public class AccountController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserService userService;
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountDTO> createUserAccount(
            @RequestBody AccountDTO accountDTO, @AuthenticationPrincipal UserDetails userDetails)
            throws ObjectNotValidException {
        logger.info("Receive POST request for creating user account {}", accountDTO);
        User user = userService.findUserByUsername(userDetails.getUsername());
        logger.info("Saving account");
        return ResponseEntity.ok(accountService.saveAccount(accountDTO, user));
    }

    @GetMapping
    public ResponseEntity<AccountResponseDTO> getUserAccount(
            @AuthenticationPrincipal UserDetails userDetails) {
        logger.info("Receive GET request for user account");
        User user = userService.findUserByUsername(userDetails.getUsername());
        logger.info("Finding accounts");
        return ResponseEntity.ok(accountService.findUserAccounts(user));
    }

    @PutMapping
    public ResponseEntity<AccountResponseDTO> updateUserAccount(
            @RequestBody AccountUpdateDTO accountUpdateDTO,
            @AuthenticationPrincipal UserDetails userDetails)
            throws ObjectNotValidException {
        logger.info("Receive PUT request for account {}", accountUpdateDTO);
        User user = userService.findUserByUsername(userDetails.getUsername());
        logger.info("Updating account");
        return ResponseEntity.ok(accountService.updateAccount(accountUpdateDTO, user));
    }
}
