package org.ntnu.idi.idatt2106.sparesti.sparestibackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yubico.webauthn.exception.AssertionFailedException;
import com.yubico.webauthn.exception.RegistrationFailedException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.token.AccessTokenResponse;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.token.LoginRegisterResponse;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user.*;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.BadInputException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.UserAlreadyExistsException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.UserNotFoundException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.validation.ObjectNotValidException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
@Tag(
        name = "authentication",
        description =
                "Endpoints for user authentication including registration, login, and token"
                        + " management.")
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

    /**
     * Initiates the biometric authentication registration process for a user.
     *
     * @param userDetails The details of the authenticated user
     * @return ResponseEntity containing the options for biometric authentication registration
     * @throws UserNotFoundException If the user is not found
     * @throws JsonProcessingException If an error occurs during JSON processing
     */
    @Operation(
            summary = "Initiate biometric authentication registration",
            description =
                    "Starts the process of registering biometric authentication for the"
                            + " authenticated user",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Options for biometric authentication registration",
                        content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))
                        })
            })
    @PostMapping(value = "/bioRegistration")
    public ResponseEntity<String> bioAuthRegistration(
            @AuthenticationPrincipal UserDetails userDetails)
            throws UserNotFoundException, JsonProcessingException {
        logger.info(
                "Received POST request to configure biometric auth by '{}'",
                userDetails.getUsername());
        String registration = authenticationService.bioAuthRegistration(userDetails.getUsername());
        logger.info("Successfully returned credential request options: {}", registration);
        return ResponseEntity.ok(registration);
    }

    /**
     * Completes the passkey authentication registration process for a user.
     *
     * @param credential The biometric authentication credential
     * @param userDetails The details of the authenticated user
     * @return ResponseEntity indicating the success of the registration process
     * @throws RegistrationFailedException If the registration fails
     * @throws IOException If an I/O error occurs
     */
    @Operation(
            summary = "Complete biometric authentication registration",
            description =
                    "Completes the process of registering biometric authentication for the"
                            + " authenticated user",
            responses = {
                @ApiResponse(
                        responseCode = "204",
                        description = "Registration completed successfully",
                        content = @Content)
            })
    @PostMapping(value = "/finishBioRegistration")
    public ResponseEntity<Void> finishRegistration(
            @RequestBody BioAuthRequest credential,
            @AuthenticationPrincipal UserDetails userDetails)
            throws RegistrationFailedException, IOException {
        logger.info(
                "Received POST request to finish registration request for '{}' with credentials:"
                        + " {}",
                userDetails.getUsername(),
                credential);
        logger.info("Saving credentials");
        authenticationService.finishBioAuthRegistration(userDetails.getUsername(), credential);
        logger.info(
                "Successfully finished registration for '{}'. Credentials have been saved.",
                userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    /**
     * Initiates the biometric authentication login process for a user.
     *
     * @param username The username of the user
     * @return ResponseEntity containing the options for biometric authentication login
     * @throws JsonProcessingException If an error occurs during JSON processing
     */
    @Operation(
            summary = "Initiate biometric authentication login",
            description =
                    "Starts the process of logging in with biometric authentication for the"
                            + " specified user",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Options for biometric authentication login",
                        content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))
                        })
            })
    @PostMapping(value = "/bioLogin/{username}")
    public ResponseEntity<String> startBioLogin(@PathVariable String username)
            throws JsonProcessingException {
        logger.info("Received POST request to login by '{}'", username);
        logger.info("Constructing credential request options.");
        String response = authenticationService.constructCredRequest(username);
        logger.info("Successfully returned credential request options: {}", response);
        return ResponseEntity.ok(response);
    }

    /**
     * Completes the biometric authentication login process for a user.
     *
     * @param username The username of the user
     * @param credential The biometric authentication credential
     * @return ResponseEntity containing access and refresh tokens upon successful login
     * @throws IOException If an I/O error occurs
     * @throws AssertionFailedException If the assertion fails
     */
    @Operation(
            summary = "Complete biometric authentication login",
            description =
                    "Completes the process of logging in with biometric authentication for the"
                            + " specified user",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Successful login",
                        content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = LoginRegisterResponse.class))
                        })
            })
    @PostMapping(value = "/finishBioLogin/{username}")
    public ResponseEntity<LoginRegisterResponse> finishLogin(
            @PathVariable String username, @RequestBody BioAuthRequest credential)
            throws IOException, AssertionFailedException {
        logger.info("Received POST request to finish login for: {}", credential);

        LoginRegisterResponse response =
                authenticationService.finishBioAuthLogin(username, credential);
        logger.info("Successfully logged in user '{}'. Returning tokens.", username);
        return ResponseEntity.ok(response);
    }
}
