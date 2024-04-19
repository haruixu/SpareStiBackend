package org.ntnu.idi.idatt2106.sparesti.sparestibackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.AuthenticationRequest;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.token.AccessTokenResponse;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.token.LoginRegisterResponse;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.BadInputException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.UserAlreadyExistsException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller used for registering and logging in a user and returning
 * access and refresh tokens
 *
 * @author Lars N, Harry X.
 * @version 1.0 - 17.4.24
 */
@RestController
@CrossOrigin
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

    /**
     * Registers a new user with a given username and password
     * @param authRequest Wrapper for username and password
     * @return ResponseEntity containing access and refresh tokens upon successful registration
     * @throws BadInputException If the username is invalid or the password is too weak
     * @throws UserAlreadyExistsException If the username is already taken
     */
    @Operation(
            summary = "Register user",
            description = "Register a new user with a username and password",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Successfully registered user",
                        content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = LoginRegisterResponse.class))
                        }),
                @ApiResponse(
                        responseCode = "400",
                        description = "Invalid username or weak password"),
                @ApiResponse(responseCode = "409", description = "Username already exists")
            })
    @PostMapping("/register")
    public ResponseEntity<LoginRegisterResponse> register(
            @Parameter(description = "Username and password") @NonNull @RequestBody
                    AuthenticationRequest authRequest)
            throws BadInputException, UserAlreadyExistsException {
        LOGGER.info("Received register request for: {}", authRequest);
        return ResponseEntity.ok(authenticationService.register(authRequest));
    }

    /**
     * Log in an existing user with username and password
     * @param authRequest Wrapper for username and password
     * @return ResponseEntity containing access and refresh tokens upon successful login
     * @throws BadInputException If the username or password is incorrect
     */
    @Operation(
            summary = "Log in user",
            description = "Log in user with username and password",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Successful log in",
                        content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = LoginRegisterResponse.class))
                        }),
                @ApiResponse(responseCode = "400", description = "Incorrect username or password")
            })
    @PostMapping("/login")
    public ResponseEntity<LoginRegisterResponse> login(
            @NonNull @RequestBody AuthenticationRequest authRequest) throws BadInputException {
        LOGGER.info("Received login request for: {}", authRequest);
        return ResponseEntity.ok(authenticationService.login(authRequest));
    }

    /**
     * Refreshes the access token using a valid refresh token.
     *
     * @param bearerToken Bearer token in authorization header
     * @return ResponseEntity containing a new access token
     */
    @PostMapping("/renewToken")
    public ResponseEntity<AccessTokenResponse> renewAccessToken(
            @RequestHeader("Authorization") String bearerToken) {
        LOGGER.info("Received renew token request for: {}", bearerToken);
        return ResponseEntity.ok(authenticationService.refreshAccessToken(bearerToken));
    }
}
