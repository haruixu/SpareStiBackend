package org.ntnu.idi.idatt2106.sparesti.sparestibackend.service;

import lombok.RequiredArgsConstructor;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.config.ChallengeConfigDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.config.ChallengeTypeConfigDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.config.UserConfigDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.config.ChallengeConfigAlreadyExistsException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.config.ChallengeConfigNotFoundException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.config.ChallengeTypeConfigAlreadyExistsException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.config.ChallengeTypeConfigNotFoundException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.config.ConfigNotFoundException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.user.UserNotFoundException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.validation.ObjectNotValidException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper.ChallengeConfigMapper;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper.ChallengeTypeConfigMapper;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper.UserConfigMapper;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.config.ChallengeConfig;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.config.ChallengeTypeConfig;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.config.UserConfig;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.repository.UserRepository;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.validation.ObjectValidator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserConfigService {

    private final UserRepository userRepository;

    private final ObjectValidator<ChallengeConfigDTO> challengeConfigValidator;
    private final ObjectValidator<ChallengeTypeConfigDTO> challengeTypeConfigValidator;
    private final ObjectValidator<UserConfigDTO> userConfigValidator;

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

    public UserConfigDTO createUserConfig(String username, UserConfigDTO request)
            throws UserNotFoundException, ObjectNotValidException {
        userConfigValidator.validate(request);
        User user = findUserByUsername(username);
        UserConfig newConfig = UserConfigMapper.INSTANCE.toEntity(request);

        user.setUserConfig(newConfig);
        userRepository.save(user);

        return UserConfigMapper.INSTANCE.toDTO(newConfig);
    }

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

    public ChallengeTypeConfigDTO createChallengeTypeConfig(
            String username, ChallengeTypeConfigDTO challengeTypeConfigDTO)
            throws ObjectNotValidException {
        challengeTypeConfigValidator.validate(challengeTypeConfigDTO);
        User user = findUserByUsername(username);

        if (!challengeConfigExists(user)) {
            throw new ChallengeConfigNotFoundException(user.getId());
        }

        String type = challengeTypeConfigDTO.type();

        if (challengeTypeConfigExists(type, user)) {
            throw new ChallengeTypeConfigAlreadyExistsException(user.getId(), type);
        }

        ChallengeTypeConfig newConfig =
                ChallengeTypeConfigMapper.INSTANCE.toEntity(challengeTypeConfigDTO);
        user.getUserConfig().getChallengeConfig().getChallengeTypeConfigs().add(newConfig);

        userRepository.save(user);

        return ChallengeTypeConfigMapper.INSTANCE.toDTO(newConfig);
    }

    public ChallengeTypeConfigDTO updateChallengeTypeConfig(
            String username, ChallengeTypeConfigDTO challengeTypeConfigDTO)
            throws ObjectNotValidException {
        challengeTypeConfigValidator.validate(challengeTypeConfigDTO);
        final User user = findUserByUsername(username);
        final String type = challengeTypeConfigDTO.type();

        ChallengeTypeConfig oldConfig = getConfig(type, user);

        ChallengeTypeConfig updatedConfig =
                ChallengeTypeConfigMapper.INSTANCE.updateEntity(oldConfig, challengeTypeConfigDTO);

        userRepository.save(user);
        return ChallengeTypeConfigMapper.INSTANCE.toDTO(updatedConfig);
    }

    public ChallengeTypeConfigDTO getChallengeTypeConfig(String type, String username) {
        User user = findUserByUsername(username);
        ChallengeTypeConfig config = getConfig(type, user);
        return ChallengeTypeConfigMapper.INSTANCE.toDTO(config);
    }

    public void deleteChallengeTypeConfig(String type, String username)
            throws UserNotFoundException, ChallengeTypeConfigNotFoundException {
        User user = findUserByUsername(username);
        ChallengeTypeConfig config = getConfig(type, user);

        user.getUserConfig().getChallengeConfig().getChallengeTypeConfigs().remove(config);
        userRepository.save(user);
    }

    private boolean challengeConfigExists(User user) {
        ChallengeConfig config = user.getUserConfig().getChallengeConfig();
        return config.getExperience() != null || config.getMotivation() != null;
    }

    private User findUserByUsername(String username) throws UserNotFoundException {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    private ChallengeTypeConfig getConfig(String type, User user) {
        return user.getUserConfig().getChallengeConfig().getChallengeTypeConfigs().stream()
                .filter(_config -> _config.getType().equalsIgnoreCase(type))
                .findAny()
                .orElseThrow(() -> new ChallengeTypeConfigNotFoundException(type));
    }

    private boolean challengeTypeConfigExists(String type, User user) {
        return user.getUserConfig().getChallengeConfig().getChallengeTypeConfigs().stream()
                .anyMatch(config -> type.equalsIgnoreCase(config.getType()));
    }
}
