package org.ntnu.idi.idatt2106.sparesti.sparestibackend.service;

import lombok.RequiredArgsConstructor;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.UserConfigDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.ConfigNotFoundException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.UserNotFoundException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper.UserConfigMapper;
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

    private User findUserByUsername(String username) throws UserNotFoundException {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }
}
