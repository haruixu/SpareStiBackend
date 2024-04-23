package org.ntnu.idi.idatt2106.sparesti.sparestibackend.service;

import lombok.RequiredArgsConstructor;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.ChallengeConfigDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.ChallengeTypeConfigDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.UserConfigDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.ChallengeConfigNotFoundException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.ChallengeTypeConfigNotFoundException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.ConfigNotFoundException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.UserNotFoundException;
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

        if (challengeConfig == null) {
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

        ChallengeTypeConfig oldConfig =
                user.getUserConfig().getChallengeConfig().getChallengeTypeConfigs().stream()
                        .filter(config -> config.getType().equalsIgnoreCase(type))
                        .findFirst()
                        .orElseThrow(() -> new ChallengeTypeConfigNotFoundException(type));

        user.getUserConfig().getChallengeConfig().getChallengeTypeConfigs().remove(oldConfig);
        ChallengeTypeConfig updatedConfig =
                ChallengeTypeConfigMapper.INSTANCE.updateEntity(oldConfig, challengeTypeConfigDTO);
        // maybe we have to do the latter
        // user.getUserConfig().getChallengeConfig().getChallengeTypeConfigs().remove(oldConfig);
        // user.getUserConfig().getChallengeConfig().getChallengeTypeConfigs().add(updatedConfig);
        userRepository.save(user);
        return ChallengeTypeConfigMapper.INSTANCE.toDTO(updatedConfig);
    }

    public ChallengeTypeConfigDTO getChallengeTypeConfig(String type, String username) {
        User user = findUserByUsername(username);
        ChallengeTypeConfig config =
                user.getUserConfig().getChallengeConfig().getChallengeTypeConfigs().stream()
                        .filter(_config -> _config.getType().equalsIgnoreCase(type))
                        .findFirst()
                        .orElseThrow(() -> new ChallengeTypeConfigNotFoundException(type));
        return ChallengeTypeConfigMapper.INSTANCE.toDTO(config);
    }

    public void deleteChallengeTypeConfig(String type, String username)
            throws UserNotFoundException, ChallengeTypeConfigNotFoundException {
        User user = findUserByUsername(username);
        ChallengeTypeConfig config =
                user.getUserConfig().getChallengeConfig().getChallengeTypeConfigs().stream()
                        .filter(_config -> _config.getType().equalsIgnoreCase(type))
                        .findFirst()
                        .orElseThrow(() -> new ChallengeTypeConfigNotFoundException(type));

        user.getUserConfig().getChallengeConfig().getChallengeTypeConfigs().remove(config);
    }

    private User findUserByUsername(String username) throws UserNotFoundException {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }
}
