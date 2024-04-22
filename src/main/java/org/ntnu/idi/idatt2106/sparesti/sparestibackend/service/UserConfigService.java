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

    public UserConfigDTO getUserConfig(Long id)
            throws UserNotFoundException, ConfigNotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        if (user.getUserConfig() == null) {
            throw new ConfigNotFoundException("No user config found for user with id " + id);
        }

        return UserConfigMapper.INSTANCE.toDTO(user.getUserConfig());
    }

    public UserConfigDTO updateUserConfig(Long id, UserConfigDTO request)
            throws UserNotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        UserConfig newConfig = UserConfigMapper.INSTANCE.toEntity(request);

        user.setUserConfig(newConfig);
        userRepository.save(user);

        return UserConfigMapper.INSTANCE.toDTO(newConfig);
    }
}
