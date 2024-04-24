package org.ntnu.idi.idatt2106.sparesti.sparestibackend.service;

import lombok.RequiredArgsConstructor;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.config.ChallengeConfigDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.config.ChallengeTypeConfigDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.config.UserConfigDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.*;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper.ChallengeConfigMapper;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper.ChallengeTypeConfigMapper;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper.UserConfigMapper;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.ChallengeConfig;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.ChallengeTypeConfig;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.UserConfig;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserConfigService {

    private final UserRepository userRepository;

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
            throws UserNotFoundException {
        User user = findUserByUsername(username);
        UserConfig newConfig = UserConfigMapper.INSTANCE.toEntity(request);

        user.setUserConfig(newConfig);
        userRepository.save(user);

        return UserConfigMapper.INSTANCE.toDTO(newConfig);
    }

    public ChallengeConfigDTO createChallengeConfig(
            String username, ChallengeConfigDTO challengeConfigDTO) throws UserNotFoundException {
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
            String username, ChallengeConfigDTO challengeConfigDTO) throws UserNotFoundException {
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
            String username, ChallengeTypeConfigDTO challengeTypeConfigDTO) {
        User user = findUserByUsername(username);

        if (getConfig(challengeTypeConfigDTO.type(), user) != null) {
            throw new ChallengeTypeConfigAlreadyExistsException(
                    user.getId(), challengeTypeConfigDTO.type());
        }

        ChallengeTypeConfig newConfig =
                ChallengeTypeConfigMapper.INSTANCE.toEntity(challengeTypeConfigDTO);
        user.getUserConfig().getChallengeConfig().getChallengeTypeConfigs().add(newConfig);

        userRepository.save(user);

        return ChallengeTypeConfigMapper.INSTANCE.toDTO(newConfig);
    }

    public ChallengeTypeConfigDTO updateChallengeTypeConfig(
            String username, ChallengeTypeConfigDTO challengeTypeConfigDTO) {
        final User user = findUserByUsername(username);
        final String type = challengeTypeConfigDTO.type();

        ChallengeTypeConfig oldConfig = getConfig(type, user);

        user.getUserConfig().getChallengeConfig().getChallengeTypeConfigs().remove(oldConfig);
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
                .findFirst()
                .orElseThrow(() -> new ChallengeTypeConfigNotFoundException(type));
    }
}
