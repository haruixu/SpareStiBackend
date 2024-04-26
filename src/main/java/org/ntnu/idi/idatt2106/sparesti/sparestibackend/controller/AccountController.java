package org.ntnu.idi.idatt2106.sparesti.sparestibackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("/accounts")
@RequiredArgsConstructor
@CrossOrigin
public class AccountController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserService userService;
    private final AccountService accountService;

    @PostMapping
    @Operation(summary = "Create User Account", description = "Creates a new user account.")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Account created successfully",
                        content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AccountDTO.class))
                        }),
                @ApiResponse(
                        responseCode = "400",
                        description = "Invalid input or request body",
                        content = @Content)
            })
    public ResponseEntity<AccountDTO> createUserAccount(
            @RequestBody AccountDTO accountDTO, @AuthenticationPrincipal UserDetails userDetails)
            throws ObjectNotValidException {
        logger.info("Receive POST request for creating user account {}", accountDTO);
        User user = userService.findUserByUsername(userDetails.getUsername());
        logger.info("Saving account");
        return ResponseEntity.ok(accountService.saveAccount(accountDTO, user));
    }

    @GetMapping
    @Operation(
            summary = "Get User Account",
            description = "Retrieves the user's account information.")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Account retrieved successfully",
                        content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AccountResponseDTO.class))
                        }),
                @ApiResponse(
                        responseCode = "404",
                        description = "Account not found",
                        content = @Content)
            })
    public ResponseEntity<AccountResponseDTO> getUserAccount(
            @AuthenticationPrincipal UserDetails userDetails) {
        logger.info("Receive GET request for user account");
        User user = userService.findUserByUsername(userDetails.getUsername());
        logger.info("Finding accounts");
        return ResponseEntity.ok(accountService.findUserAccounts(user));
    }

    @PutMapping
    @Operation(
            summary = "Update User Account",
            description = "Updates the user's account information.")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Account updated successfully",
                        content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AccountResponseDTO.class))
                        }),
                @ApiResponse(
                        responseCode = "400",
                        description = "Invalid input or request body",
                        content = @Content),
                @ApiResponse(
                        responseCode = "404",
                        description = "Account not found",
                        content = @Content)
            })
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
