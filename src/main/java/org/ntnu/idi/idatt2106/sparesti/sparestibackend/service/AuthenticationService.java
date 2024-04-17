package org.ntnu.idi.idatt2106.sparesti.sparestibackend.service;

import lombok.RequiredArgsConstructor;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.AuthenticationRequest;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.token.AccessTokenRequest;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.token.AccessTokenResponse;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.token.LoginRegisterResponse;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.InvalidTokenException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.UserAlreadyExistsException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Role;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.security.JWTService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager manager;

    public LoginRegisterResponse register(AuthenticationRequest request)
            throws UserAlreadyExistsException {
        if (userService.userExists(request.getUsername())) {
            throw new UserAlreadyExistsException(
                    "User with username: " + request.getUsername() + " already exists");
        }
        User user =
                User.builder()
                        .username(request.getUsername())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .role(Role.USER)
                        .build();
        userService.save(user);
        String jwtAccessToken = jwtService.generateToken(user, 5);
        String jwtRefreshToken = jwtService.generateToken(user, 30);
        return LoginRegisterResponse.builder()
                .accessToken(jwtAccessToken)
                .refreshToken(jwtRefreshToken)
                .build();
    }

    public LoginRegisterResponse login(AuthenticationRequest request) {
        manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()));
        User user = userService.findUserByUsername(request.getUsername());
        String jwtAccessToken = jwtService.generateToken(user, 5);
        String jwtRefreshToken = jwtService.generateToken(user, 30);
        return LoginRegisterResponse.builder()
                .accessToken(jwtAccessToken)
                .refreshToken(jwtRefreshToken)
                .build();
    }

    public AccessTokenResponse refreshAccessToken(AccessTokenRequest request) {
        User user =
                userService.findUserByUsername(
                        jwtService.extractUsername(request.getRefreshToken()));
        String newJWTAccessToken;

        if (jwtService.isTokenValid(request.getRefreshToken(), user)) {
            newJWTAccessToken = jwtService.generateToken(user, 5);
        } else {
            throw new InvalidTokenException("Token is invalid");
        }

        return AccessTokenResponse.builder().accessToken(newJWTAccessToken).build();
    }
}
