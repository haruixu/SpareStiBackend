package org.ntnu.idi.idatt2106.sparesti.sparestibackend.service;

import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.AuthenticationRequest;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.RegisterRequest;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.token.AccessTokenResponse;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.token.LoginRegisterResponse;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.BadInputException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.UserAlreadyExistsException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper.RegisterMapper;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.Role;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.security.JWTService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service responsible for registering a new user, logging in an existing user and refreshing
 * a users access token
 *
 * @author Harry L.X. & Lars M.L.N
 * @version 1.0
 * @since 17.4.24
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager manager;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    /**
     * Registers a new, valid user. For a user to be valid, they have to
     * have a valid and unique username, a valid and unique email, valid first name and last names,
     * and a strong password.
     * @param request Wrapper for user information used for registering
     * @return Jwt tokens for the registered user
     * @throws BadInputException If the user information is invalid (username, first/last names, email) of if password is weak
     * @throws UserAlreadyExistsException If username or email have been taken
     */
    public LoginRegisterResponse register(RegisterRequest request)
            throws BadInputException, UserAlreadyExistsException {
        if (!(isUsernameValid(request.getUsername()))) {
            throw new BadInputException(
                    "The username can only contain letters, numbers and underscore, "
                            + "with the first character being a letter. "
                            + "The length must be between 3 and 30 characters");
        }
        if (!isEmailValid(request.getEmail())) {
            throw new BadInputException("The email address is invalid.");
        }
        if (!isNameValid(request.getFirstName())) {
            throw new BadInputException(
                    "The first name: '" + request.getFirstName() + "' is invalid.");
        }
        if (!isNameValid(request.getLastName())) {
            throw new BadInputException(
                    "The last name: '" + request.getLastName() + "' is invalid.");
        }
        if (userService.userExistsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException(
                    "User with username: " + request.getUsername() + " already exists");
        }
        if (userService.userExistByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException(
                    "User with email: " + request.getEmail() + " already exists");
        }
        if (!isPasswordStrong(request.getPassword())) {
            throw new BadInputException(
                    "Password must be at least 8 characters long, include numbers, upper and lower"
                            + " case letters, and at least one special character");
        }

        logger.info("Creating user");
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = RegisterMapper.INSTANCE.toEntity(request, Role.USER, encodedPassword);
        logger.info("Saving user");
        userService.save(user);
        logger.info("Generating tokens");
        String jwtAccessToken = jwtService.generateToken(user, 5);
        String jwtRefreshToken = jwtService.generateToken(user, 30);
        return RegisterMapper.INSTANCE.toDTO(user, jwtAccessToken, jwtRefreshToken);
    }

    /**
     * Checks if username is valid.
     * Valid username starts with a letter.
     * Valid characters are letters, numbers and underscore.
     * Length must be between 3 and 30 characters.
     * @param username Username
     * @return True, if username is valid. Else, returns false.
     */
    private boolean isUsernameValid(String username) {
        String usernamePattern = "^[A-Za-z][A-Za-z0-9_]{2,29}$";
        return Pattern.compile(usernamePattern).matcher(username).matches();
    }

    /**
     * Checks if email is invalid.
     * Valid email must start with letters, numbers, underscore, '+', '&', '*', '-'
     * Valid email can include must include '@'
     * Must include a period after '@'
     * Must have letters after period of length 2-7 characters
     * @param email Email
     * @return True, if email is valid.
     */
    public boolean isEmailValid(String email) {
        String emailPattern =
                "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.compile(emailPattern).matcher(email).matches();
    }

    /**
     * Checks if name is valid
     * A valid contains can only contain characters, white space,
     * comma, period, singe quotes and hyphens
     * @param name Name
     * @return If name is valid
     */
    public boolean isNameValid(String name) {
        String namePattern = "^[a-zA-Z ,.'-]+$";
        return Pattern.compile(namePattern).matcher(name).matches();
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

    /**
     * Log in user with credentials (username and password)
     * @param request Wrapper for username and password
     * @return Jwt tokens upon successful login
     * @throws BadInputException if no user has a matching username or password
     */
    public LoginRegisterResponse login(AuthenticationRequest request) throws BadInputException {
        if (!userService.userExistsByUsername(request.getUsername())
                || !matches(
                        request.getPassword(),
                        userService.findUserByUsername(request.getUsername()).getPassword())) {
            throw new BadInputException("Username or password is incorrect");
        }

        logger.info("Setting authentication context");
        manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()));
        User user = userService.findUserByUsername(request.getUsername());
        System.out.println("Generating tokens");
        String jwtAccessToken = jwtService.generateToken(user, 5);
        String jwtRefreshToken = jwtService.generateToken(user, 30);
        return RegisterMapper.INSTANCE.toDTO(user, jwtAccessToken, jwtRefreshToken);
    }

    /**
     * Checks if an input password matches the stored (encrypted) password.
     *
     * @param inputPassword
     *            The input password to check
     * @param storedPassword
     *            The stored (encrypted) password to compare with
     * @return true if the input password matches the stored password, false otherwise
     */
    public boolean matches(String inputPassword, String storedPassword) {
        return passwordEncoder.matches(inputPassword, storedPassword);
    }

    /**
     * Refreshes access token given a valid refresh token
     * @param bearerToken Stringified HTTP-header (Authorization-header)
     * @return Access token wrapper if the refresh token is valid
     * @throws UsernameNotFoundException If the tokens subject matches no existing username
     */
    public AccessTokenResponse refreshAccessToken(String bearerToken)
            throws UsernameNotFoundException {
        // TODO: Add config class for handling MalformedJwtException
        String parsedRefreshToken = bearerToken.substring(7);
        User user = userService.findUserByUsername(jwtService.extractUsername(parsedRefreshToken));
        String newJWTAccessToken = jwtService.generateToken(user, 5);
        return AccessTokenResponse.builder().accessToken(newJWTAccessToken).build();
    }
}
