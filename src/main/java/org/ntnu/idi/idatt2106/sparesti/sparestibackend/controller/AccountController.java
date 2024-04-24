package org.ntnu.idi.idatt2106.sparesti.sparestibackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.AccountDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.AccountResponseDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.AccountUpdateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.BadInputException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.service.AccountService;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.service.UserService;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.util.ApplicationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
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
            @Valid @RequestBody AccountDTO accountDTO,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetails userDetails) {
        if (bindingResult.hasErrors()) {
            throw new BadInputException(ApplicationUtil.BINDING_RESULT_ERROR);
        }
        User user = userService.findUserByUsername(userDetails.getUsername());
        return ResponseEntity.ok(accountService.saveAccount(accountDTO, user));
    }

    @GetMapping
    public ResponseEntity<AccountResponseDTO> getUserAccount(
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findUserByUsername(userDetails.getUsername());
        return ResponseEntity.ok(accountService.findUserAccounts(user));
    }

    @PutMapping
    public ResponseEntity<AccountResponseDTO> updateUserAccount(
            @Valid @RequestBody AccountUpdateDTO accountUpdateDTO,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetails userDetails) {
        if (bindingResult.hasErrors()) {
            throw new BadInputException(ApplicationUtil.BINDING_RESULT_ERROR);
        }
        User user = userService.findUserByUsername(userDetails.getUsername());
        return ResponseEntity.ok(accountService.updateAccount(accountUpdateDTO, user));
    }
}
