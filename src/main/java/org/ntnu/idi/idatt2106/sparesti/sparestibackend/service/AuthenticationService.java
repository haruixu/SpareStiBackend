package org.ntnu.idi.idatt2106.sparesti.sparestibackend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yubico.webauthn.*;
import com.yubico.webauthn.data.*;
import com.yubico.webauthn.exception.AssertionFailedException;
import com.yubico.webauthn.exception.RegistrationFailedException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.token.AccessTokenResponse;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.token.LoginRegisterResponse;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user.AuthenticationRequest;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user.BioAuthRequest;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user.RegisterRequest;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.*;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.validation.ObjectNotValidException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper.BioAuthMapper;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper.RegisterMapper;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Authenticator;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.Role;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.repository.AuthenticatorRepository;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.security.JWTService;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.util.ApplicationUtil;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.validation.ObjectValidator;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.validation.user.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Service responsible for registering a new user, logging in an existing user and refreshing
 * a users access token
 *
 * @author Harry L.X. and Lars M.L.N
 * @version 1.0
 * @since 17.4.24
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final BioAuthMapper bioAuthMapper;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager manager;
    private final RelyingParty relyingParty;
    private final AuthenticatorRepository authRepository;
    private final Map<String, PublicKeyCredentialCreationOptions> requestOptionMap =
            new HashMap<>();
    private final Map<String, AssertionRequest> assertionRequestMap = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private static final int ONE_DAY_IN_MINUTES = 60 * 24;
    private static final int ONE_WEEK_IN_MINUTES = ONE_DAY_IN_MINUTES * 7;

    private final UserValidator<RegisterRequest> registerRequestValidator;
    private final ObjectValidator<AuthenticationRequest> authenticationRequestValidator;
    private final AuthenticatorRepository authenticatorRepository;

    /**
     * Registers a new, valid user. For a user to be valid, they have to
     * have a valid and unique username, a valid and unique email, valid first name and last names,
     * and a strong password.
     *
     * @param request Wrapper for user information used for registering
     * @return Jwt tokens for the registered user
     * @throws BadInputException          If the user information is invalid (username, first/last names, email) of if password is weak
     * @throws UserAlreadyExistsException If username or email have been taken
     */
    public LoginRegisterResponse register(RegisterRequest request)
            throws BadInputException, UserAlreadyExistsException, ObjectNotValidException {
        registerRequestValidator.validate(request);
        logger.info("Creating user");
        String encodedPassword = passwordEncoder.encode(request.password());
        User user = RegisterMapper.INSTANCE.toEntity(request, Role.USER, encodedPassword);
        logger.info("Saving user with username '{}'", user.getUsername());
        userService.save(user);
        logger.info("Generating tokens");
        String jwtAccessToken = jwtService.generateToken(user, ONE_DAY_IN_MINUTES);
        String jwtRefreshToken = jwtService.generateToken(user, ONE_WEEK_IN_MINUTES);
        return RegisterMapper.INSTANCE.toDTO(user, jwtAccessToken, jwtRefreshToken);
    }

    /**
     * Log in user with credentials (username and password)
     *
     * @param request Wrapper for username and password
     * @return Jwt tokens upon successful login
     * @throws ObjectNotValidException If request object is invalid
     * @throws BadCredentialsException If credentials don't match
     */
    public LoginRegisterResponse login(AuthenticationRequest request)
            throws ObjectNotValidException, BadCredentialsException {
        authenticationRequestValidator.validate(request);
        // Check credentials
        manager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        User user = userService.findUserByUsername(request.username());
        System.out.println("Generating tokens for user: " + user);
        String jwtAccessToken = jwtService.generateToken(user, ONE_DAY_IN_MINUTES);
        String jwtRefreshToken = jwtService.generateToken(user, ONE_WEEK_IN_MINUTES);
        return RegisterMapper.INSTANCE.toDTO(user, jwtAccessToken, jwtRefreshToken);
    }

    /**
     * Refreshes access token given a valid refresh token
     *
     * @param bearerToken Stringified HTTP-header (Authorization-header)
     * @return Access token wrapper if the refresh token is valid
     * @throws UserNotFoundException If the tokens subject matches no existing username
     */
    public AccessTokenResponse refreshAccessToken(String bearerToken) throws UserNotFoundException {
        String parsedRefreshToken = bearerToken.substring(7);
        String userName = jwtService.extractUsername(parsedRefreshToken);
        User user = userService.findUserByUsername(userName);
        String newJWTAccessToken = jwtService.generateToken(user, 5);
        return new AccessTokenResponse(newJWTAccessToken);
    }

    /**
     * Initiates a new biometric authentication registration process for the specified user.
     *
     * @param username The username of the user for whom biometric authentication registration is initiated.
     * @return A JSON string representing the registration request for biometric authentication.
     * @throws JsonProcessingException If an error occurs during JSON processing.
     */
    public String bioAuthRegistration(String username) throws JsonProcessingException {
        User user = userService.findUserByUsername(username);

        if (Optional.ofNullable(user.getHandle()).isPresent()) {
            authenticatorRepository.removeAllByUser(user);
        }

        UserIdentity userIdentity =
                UserIdentity.builder()
                        .name(user.getUsername())
                        .displayName(user.getFirstName() + " " + user.getLastName())
                        .id(ApplicationUtil.generateRandom(32))
                        .build();

        user.setHandle(userIdentity.getId());
        userService.save(user);
        StartRegistrationOptions registrationOptions =
                StartRegistrationOptions.builder().user(userIdentity).build();
        PublicKeyCredentialCreationOptions registration =
                relyingParty.startRegistration(registrationOptions);
        this.requestOptionMap.put(user.getUsername(), registration);
        return registration.toCredentialsCreateJson();
    }

    /**
     * Finishes the biometric authentication registration process.
     *
     * @param username   The username of the user.
     * @param credential The biometric authentication credential.
     * @throws IOException              If an I/O error occurs.
     * @throws RegistrationFailedException If the registration fails.
     * @throws JsonProcessingException  If an error occurs during JSON processing.
     */
    public void finishBioAuthRegistration(String username, BioAuthRequest credential)
            throws IOException, RegistrationFailedException, JsonProcessingException {
        User user = userService.findUserByUsername(username);

        String credentialString = bioAuthMapper.credentialToString(credential);

        PublicKeyCredentialCreationOptions requestOptions =
                Optional.ofNullable(requestOptionMap.get(username))
                        .orElseThrow(
                                () ->
                                        new ResponseStatusException(
                                                HttpStatus.INTERNAL_SERVER_ERROR,
                                                "Cached request expired. Try to register again!"));
        PublicKeyCredential<AuthenticatorAttestationResponse, ClientRegistrationExtensionOutputs>
                pkc = PublicKeyCredential.parseRegistrationResponseJson(credentialString);

        FinishRegistrationOptions options =
                FinishRegistrationOptions.builder().request(requestOptions).response(pkc).build();
        RegistrationResult result = relyingParty.finishRegistration(options);

        Authenticator savedAuth =
                new Authenticator(result, pkc.getResponse(), user, user.getUsername());
        authRepository.save(savedAuth);
    }

    /**
     * Constructs a biometric authentication credential request for the specified username.
     *
     * @param username The username of the user.
     * @return The JSON representation of the credential request.
     * @throws JsonProcessingException If an error occurs during JSON processing.
     */
    public String constructCredRequest(String username) throws JsonProcessingException {
        AssertionRequest request =
                relyingParty.startAssertion(
                        StartAssertionOptions.builder().username(username).build());
        this.assertionRequestMap.put(username, request);
        return request.toCredentialsGetJson();
    }

    /**
     * Finishes the biometric authentication login process.
     *
     * @param username   The username of the user.
     * @param credential The biometric authentication credential.
     * @return The response containing user information and JWT tokens.
     * @throws IOException                      If an I/O error occurs.
     * @throws AssertionFailedException         If the assertion fails.
     * @throws BadCredentialsException         If the biometric authentication fails.
     * @throws AssertionRequestNotFoundException If the assertion request is not found.
     */
    public LoginRegisterResponse finishBioAuthLogin(String username, BioAuthRequest credential)
            throws IOException,
                    AssertionFailedException,
                    BadCredentialsException,
                    AssertionRequestNotFoundException {

        PublicKeyCredential<AuthenticatorAssertionResponse, ClientAssertionExtensionOutputs> pkc;

        pkc =
                PublicKeyCredential.parseAssertionResponseJson(
                        bioAuthMapper.credentialToString(credential));
        AssertionRequest request =
                Optional.ofNullable(this.assertionRequestMap.get(username))
                        .orElseThrow(() -> new AssertionRequestNotFoundException(username));
        AssertionResult result =
                relyingParty.finishAssertion(
                        FinishAssertionOptions.builder().request(request).response(pkc).build());
        if (!result.isSuccess()) {
            throw new BadCredentialsException("Biometric authentication failed.");
        }

        User user = userService.findUserByUsername(username);
        String jwtAccessToken = jwtService.generateToken(user, ONE_DAY_IN_MINUTES);
        String jwtRefreshToken = jwtService.generateToken(user, ONE_WEEK_IN_MINUTES);
        return RegisterMapper.INSTANCE.toDTO(user, jwtAccessToken, jwtRefreshToken);
    }
}
