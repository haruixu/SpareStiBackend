package org.ntnu.idi.idatt2106.sparesti.sparestibackend.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.AuthenticationRequest;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.token.AccessTokenRequest;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.token.AccessTokenResponse;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.token.LoginRegisterResponse;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

    @PostMapping("/register")
    public ResponseEntity<LoginRegisterResponse> register(
            @NonNull @RequestBody AuthenticationRequest authRequest) {
        LOGGER.debug("Received register request for: {}", authRequest);
        return ResponseEntity.ok(authenticationService.register(authRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginRegisterResponse> login(
            @NonNull @RequestBody AuthenticationRequest authRequest) {
        LOGGER.debug("Received login request for: {}", authRequest);
        return ResponseEntity.ok(authenticationService.login(authRequest));
    }

    @PostMapping("/renewToken")
    public ResponseEntity<AccessTokenResponse> renewAccessToken(
            @NonNull @RequestBody AccessTokenRequest accessTokenRequest) {
        LOGGER.debug("Received renew token request for: {}", accessTokenRequest);
        return ResponseEntity.ok(authenticationService.refreshAccessToken(accessTokenRequest));
    }
}
