package org.ntnu.idi.idatt2106.sparesti.sparestibackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.token.AccessTokenResponse;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.token.LoginRegisterResponse;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user.AuthenticationRequest;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user.RegisterRequest;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.BadInputException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.UserAlreadyExistsException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.validation.ObjectNotValidException;
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
 * @version 1.0
 * @since 17.4.24
 */
@RestController
@CrossOrigin
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    /**
     * Registers a new user with a given username, password, email, first name and last name
     * @param registerRequest Wrapper for user info
     * @return ResponseEntity containing access and refresh tokens upon successful registration
     * @throws BadInputException If the username, first name last name or email is invalid or the password is too weak
     * @throws UserAlreadyExistsException If the username is already taken
     */
    @Tag(name = "token", description = "CRUD methods related to JWT tokens")
    @Operation(
            summary = "Register user",
            description =
                    "Register a new user with a username, password, email, first name and last"
                            + " name",
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
                        description =
                                "Invalid username, first name, last name or email or weak"
                                        + " password",
                        content = @Content),
                @ApiResponse(
                        responseCode = "409",
                        description = "Username already exists",
                        content = @Content),
            })
    @PostMapping("/register")
    public ResponseEntity<LoginRegisterResponse> register(
            @RequestBody RegisterRequest registerRequest)
            throws BadInputException, ObjectNotValidException, UserAlreadyExistsException {
        logger.info("Received register request for: {}", registerRequest);
        LoginRegisterResponse responseContent = authenticationService.register(registerRequest);
        logger.info("Successfully registered user");
        return ResponseEntity.ok(responseContent);
    }

    /**
     * Log in an existing user with username and password
     * @param authRequest Wrapper for username and password
     * @return ResponseEntity containing access and refresh tokens upon successful login
     * @throws BadInputException If the username or password is incorrect
     */
    @Tag(name = "token", description = "CRUD methods related to JWT tokens")
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
                @ApiResponse(
                        responseCode = "400",
                        description = "Incorrect username or password",
                        content = @Content)
            })
    @PostMapping("/login")
    public ResponseEntity<LoginRegisterResponse> login(
            @RequestBody AuthenticationRequest authRequest)
            throws BadInputException, ObjectNotValidException {
        logger.info("Received login request for: {}", authRequest);
        LoginRegisterResponse responseContent = authenticationService.login(authRequest);
        logger.info("Successfully logged in user");
        return ResponseEntity.ok(responseContent);
    }

    /**
     * Refreshes the access token using a valid refresh token.
     *
     * @param bearerToken Bearer token in authorization header
     * @return ResponseEntity containing a new access token
     */
    @Operation(
            summary = "Refreshes access token",
            description =
                    "Returns a new access token if the request contains a valid refresh token",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Successful renewal",
                        content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AccessTokenResponse.class))
                        })
            })
    @Tag(name = "token", description = "CRUD methods related to JWT tokens")
    @GetMapping("/renewToken")
    public ResponseEntity<AccessTokenResponse> renewAccessToken(
            @Parameter(description = "Authorization header with bearer token")
                    @RequestHeader("Authorization")
                    String bearerToken) {
        logger.info("Received renew token request for: {}", bearerToken);
        AccessTokenResponse responseContent = authenticationService.refreshAccessToken(bearerToken);
        logger.info("Successfully renewed access token");
        return ResponseEntity.ok(responseContent);
    }
}
