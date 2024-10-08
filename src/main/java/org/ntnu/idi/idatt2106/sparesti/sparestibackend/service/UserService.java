package org.ntnu.idi.idatt2106.sparesti.sparestibackend.service;

import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user.StreakResponse;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user.UserResponse;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user.UserUpdateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.*;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.user.UserNotFoundException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.validation.BadInputException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper.UserMapper;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Challenge;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.repository.UserRepository;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.validation.user.UserValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Handles business logic related to the user entity. Adds an extra layer abstraction between
 * controller and the data layer.
 *
 * @author Harry L.X and Lars M.L.N
 * @version 1.0
 * @since 17.4.24
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserValidator<UserUpdateDTO> userUpdateValidator;

    /**
     * Persists a user entity
     * @param user User entity
     * @return The saved user entity
     */
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * Finds a user from a given username.
     * @param username Username used for finding a user
     * @return User entity with matching username
     * @throws UserNotFoundException If no user has the given username
     */
    public User findUserByUsername(String username) throws UserNotFoundException {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    /**
     * Finds user by their username
     * @param username Username of user
     * @return User with matching username
     */
    public UserResponse findUserByUsernameToDTO(String username) {
        return UserMapper.INSTANCE.toDTO(
                userRepository
                        .findByUsername(username)
                        .orElseThrow(() -> new UserNotFoundException(username)));
    }

    /**
     * Find user by their email
     * @param email Email of user
     * @return User with matching email
     * @throws EmailNotFoundException If email could not be found
     */
    public User findUserByEmail(String email) throws EmailNotFoundException {
        return userRepository
                .findByEmail(email)
                .orElseThrow(
                        () ->
                                new EmailNotFoundException(
                                        "User with email: " + email + " not found"));
    }

    /**
     * Updates a user specified by their username
     * @param username username of user
     * @param updateDTO Updated user changes
     * @return Updated user
     */
    public UserResponse updateUser(String username, UserUpdateDTO updateDTO) {
        userUpdateValidator.validate(updateDTO);
        User user = findUserByUsername(username);

        String newPassword = null;
        if (updateDTO.password() != null) {
            newPassword = passwordEncoder.encode(updateDTO.password());
        }
        UserMapper.INSTANCE.updateEntity(user, updateDTO, newPassword);
        userRepository.save(user);
        return UserMapper.INSTANCE.toDTO(user);
    }

    /**
     * Determines whether a user with the given email exists
     * @param email Email
     * @return True, if a user with the given email exists
     */
    public boolean userExistByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    /**
     * Updates a user's password
     * @param userID Identifies a user
     * @param newPassword New password of user
     */
    public void updatePassword(Long userID, String newPassword) {
        User user =
                userRepository
                        .findById(userID)
                        .orElseThrow(() -> new BadInputException("User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    /**
     * Gets the streak of a user, identified by their username
     * @param username Username of user
     * @return Streak of the user
     * @throws UserNotFoundException If the user could not be found
     */
    public StreakResponse getStreak(String username) throws UserNotFoundException {
        User user = findUserByUsername(username);

        boolean resetStreak =
                user.getChallenges().stream()
                        .filter(challenge -> challenge.getDue() != null)
                        .anyMatch(
                                challenge ->
                                        challenge.getCompletedOn() == null
                                                && challenge
                                                        .getDue()
                                                        .isBefore(ZonedDateTime.now()));

        if (resetStreak) {
            user.setStreak(0L);
            user.setStreakStart(null);
        }

        ZonedDateTime firstDue =
                user.getChallenges().stream()
                        .map(Challenge::getDue)
                        .filter(due -> due.isAfter(ZonedDateTime.now()))
                        .sorted()
                        .findFirst()
                        .orElse(null);

        return UserMapper.INSTANCE.toStreakResponse(user, firstDue);
    }
}
