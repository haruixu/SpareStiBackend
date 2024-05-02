package org.ntnu.idi.idatt2106.sparesti.sparestibackend.service;

import lombok.RequiredArgsConstructor;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.config.ChallengeConfigDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.config.UserConfigDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.*;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.validation.ObjectNotValidException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper.ChallengeConfigMapper;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper.UserConfigMapper;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.ChallengeConfig;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.repository.UserRepository;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.validation.user.ChallengeConfigValidator;
import org.springframework.stereotype.Service;

/**
 * Service class for handling business logic related to Userconfig entity and DTO's
 *
 * @author Yasin M.
 * @version 1.0
 * @since 19.4.24
 */
@Service
@RequiredArgsConstructor
public class UserConfigService {

    private final UserRepository userRepository;

    private final ChallengeConfigValidator challengeConfigValidator;

    /**
     * Gets the config of a user
     * @param username Username of user
     * @return UserConfig for the user
     * @throws UserNotFoundException If the user could not be found from the username
     * @throws ConfigNotFoundException If the user has yet to set up their config
     */
    public UserConfigDTO getUserConfig(String username)
            throws UserNotFoundException, ConfigNotFoundException {
        User user = findUserByUsername(username);
        if (user.getUserConfig() == null) {
            throw new ConfigNotFoundException(
                    "No user config found for user with username: '" + username + "'");
        }

        if (!challengeConfigExists(user)) {
            user.getUserConfig().setChallengeConfig(null);
        }

        return UserConfigMapper.INSTANCE.toDTO(user.getUserConfig());
    }

    /**
     * Creates a challenge config for a user
     * @param username Username of user
     * @param challengeConfigDTO The user's new challenge config info
     * @return Created config info
     * @throws UserNotFoundException If the user could not be found by the username
     * @throws ObjectNotValidException If the config dto fields are invalid
     */
    public ChallengeConfigDTO createChallengeConfig(
            String username, ChallengeConfigDTO challengeConfigDTO)
            throws UserNotFoundException, ObjectNotValidException {
        challengeConfigValidator.validate(challengeConfigDTO);
        User user = findUserByUsername(username);

        if (challengeConfigExists(user)) {
            throw new ChallengeConfigAlreadyExistsException(user.getId());
        }

        ChallengeConfig challengeConfig =
                ChallengeConfigMapper.INSTANCE.toEntity(challengeConfigDTO);
        user.getUserConfig().setChallengeConfig(challengeConfig);

        User persistedUser = userRepository.save(user);

        return ChallengeConfigMapper.INSTANCE.toDTO(
                persistedUser.getUserConfig().getChallengeConfig());
    }

    /**
     * Gets the challenge config of a user
     * @param username Username of user
     * @return Challenge config of user
     * @throws ChallengeConfigNotFoundException If the user has yet to set up their challenge config
     * @throws UserNotFoundException If the user could not be found from the username
     */
    public ChallengeConfigDTO getChallengeConfig(String username)
            throws ChallengeConfigNotFoundException, UserNotFoundException {
        User user = findUserByUsername(username);
        ChallengeConfig challengeConfig = user.getUserConfig().getChallengeConfig();

        if (!challengeConfigExists(user)) {
            throw new ChallengeConfigNotFoundException(user.getId());
        }

        return ChallengeConfigMapper.INSTANCE.toDTO(challengeConfig);
    }

    /**
     * Method will reset all challenge type configs. make sure to send intact challenge type configs in the dto.
     * This behaviour can be disabled, by ignoring the challenge type dtos in the mapper.
     * @param username Username of user
     * @param challengeConfigDTO Challenge config with new changes
     * @return Updated challenge config
     * @throws UserNotFoundException If the user could not be found from the username
     * @throws ObjectNotValidException If the dto fields are invalid
     */
    public ChallengeConfigDTO updateChallengeConfig(
            String username, ChallengeConfigDTO challengeConfigDTO)
            throws UserNotFoundException, ObjectNotValidException {
        challengeConfigValidator.validate(challengeConfigDTO);
        User user = findUserByUsername(username);

        if (!challengeConfigExists(user)) {
            throw new ChallengeConfigNotFoundException(user.getId());
        }

        ChallengeConfig challengeConfig = user.getUserConfig().getChallengeConfig();
        ChallengeConfig updatedChallengeConfig =
                ChallengeConfigMapper.INSTANCE.updateEntity(challengeConfig, challengeConfigDTO);
        user.getUserConfig().setChallengeConfig(updatedChallengeConfig);
        userRepository.save(user);

        return ChallengeConfigMapper.INSTANCE.toDTO(updatedChallengeConfig);
    }

    /**
     * Checks if challenge config exists for user
     * @param user The user
     * @return Returns true, if it exists
     */
    private boolean challengeConfigExists(User user) {
        ChallengeConfig config = user.getUserConfig().getChallengeConfig();
        return config.getExperience() != null || config.getMotivation() != null;
    }

    /**
     * Finds a user by the username
     * @param username Username of user
     * @return User with matching username
     * @throws UserNotFoundException User could not be found from the username User could not be found from the username
     */
    private User findUserByUsername(String username) throws UserNotFoundException {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }
}
