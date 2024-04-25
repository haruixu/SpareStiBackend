package org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.config.ChallengeConfigDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.config.UserConfigDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper.config.ChallengeConfigMapper;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper.config.UserConfigMapper;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.config.ChallengeConfig;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.config.Experience;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.config.Motivation;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.config.Role;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.config.UserConfig;

@ExtendWith(MockitoExtension.class)
public class UserConfigMapperTest {

    @Mock private ChallengeConfigMapper challengeConfigMapper;

    @InjectMocks private UserConfigMapper userConfigMapper = UserConfigMapper.INSTANCE;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testToDTO() {

        ChallengeConfig challengeConfig =
                new ChallengeConfig(Experience.MEDIUM, Motivation.MEDIUM, null);
        ChallengeConfigDTO challengeConfigDTO =
                new ChallengeConfigDTO(Experience.MEDIUM, Motivation.MEDIUM, null);

        when(challengeConfigMapper.toDTO(challengeConfig)).thenReturn(challengeConfigDTO);

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

        when(challengeConfigMapper.toEntity(challengeConfigDTO)).thenReturn(challengeConfig);

        UserConfigDTO userConfigDTO = new UserConfigDTO(Role.ADMIN, challengeConfigDTO);

        UserConfig userConfig = userConfigMapper.toEntity(userConfigDTO);

        assertEquals(Role.ADMIN, userConfig.getRole());
        assertEquals(challengeConfig, userConfig.getChallengeConfig());
    }
}
