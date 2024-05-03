package org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.config.ChallengeConfigDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.config.UserConfigDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.ChallengeConfig;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.UserConfig;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.Experience;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.Motivation;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.Role;

@ExtendWith(MockitoExtension.class)
public class UserConfigMapperTest {

    private final UserConfigMapper userConfigMapper = UserConfigMapper.INSTANCE;

    @Test
    public void testToDTO() {

        ChallengeConfig challengeConfig =
                new ChallengeConfig(Experience.MEDIUM, Motivation.MEDIUM, null);
        ChallengeConfigDTO challengeConfigDTO =
                new ChallengeConfigDTO(Experience.MEDIUM, Motivation.MEDIUM, null);

        UserConfig userConfig = new UserConfig(Role.USER, challengeConfig);

        UserConfigDTO userConfigResponse = userConfigMapper.toDTO(userConfig);

        assertEquals(0, userConfig.getRole().compareTo(Role.USER));
        assertEquals(challengeConfigDTO, userConfigResponse.challengeConfig());
    }

    @Test
    public void testToEntity() {
        ChallengeConfigDTO challengeConfigDTO =
                new ChallengeConfigDTO(Experience.MEDIUM, Motivation.MEDIUM, null);
        ChallengeConfig challengeConfig =
                new ChallengeConfig(Experience.MEDIUM, Motivation.MEDIUM, null);

        UserConfigDTO userConfigDTO = new UserConfigDTO(Role.ADMIN, challengeConfigDTO);

        UserConfig userConfig = userConfigMapper.toEntity(userConfigDTO);

        assertEquals(Role.ADMIN, userConfig.getRole());

        assertEquals(challengeConfig.getExperience(), challengeConfigDTO.experience());
        assertEquals(challengeConfig.getMotivation(), challengeConfigDTO.motivation());
        assertEquals(
                challengeConfig.getChallengeTypeConfigs(),
                challengeConfigDTO.challengeTypeConfigs());
    }

    @Test
    public void testToEntityAndDTOWithNull() {
        assertNull(userConfigMapper.toDTO(null));
        assertNull(userConfigMapper.toEntity(null));
    }
}
