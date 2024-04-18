package org.ntnu.idi.idatt2106.sparesti.sparestibackend.service;

import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.AuthenticationRequest;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.token.AccessTokenResponse;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.token.LoginRegisterResponse;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.BadInputException;
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
        if (!isPasswordStrong(request.getPassword())) {
            throw new BadInputException(
                    "Password must be at least 8 characters long, include numbers, upper and lower"
                            + " case letters, and at least one special character");
        }
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

    /**
     * Checks if a password meets the strength criteria.
     *
     * @param password
     *            The password to check
     * @return true if the password meets the criteria, false otherwise
     */
    private boolean isPasswordStrong(String password) {
        // Example criteria: at least 8 characters, including numbers, letters and at least one
        // special character
        String passwordPattern =
                "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        return Pattern.compile(passwordPattern).matcher(password).matches();
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

    public AccessTokenResponse refreshAccessToken(String bearerToken) {
        String parsedRefreshToken = bearerToken.substring(7);
        User user = userService.findUserByUsername(jwtService.extractUsername(parsedRefreshToken));
        String newJWTAccessToken = jwtService.generateToken(user, 5);
        return AccessTokenResponse.builder().accessToken(newJWTAccessToken).build();
    }
}
