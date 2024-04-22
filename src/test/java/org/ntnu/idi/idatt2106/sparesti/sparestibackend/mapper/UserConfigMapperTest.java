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
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.ChallengeConfigDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.UserConfigDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.ChallengeConfig;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.UserConfig;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.Experience;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.Motivation;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.Role;

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
                new ChallengeConfig(Experience.MIDDLE, Motivation.MIDDLE, null);
        ChallengeConfigDTO challengeConfigDTO =
                new ChallengeConfigDTO(Experience.MIDDLE, Motivation.MIDDLE, null);

        when(challengeConfigMapper.toDTO(challengeConfig)).thenReturn(challengeConfigDTO);

        UserConfig userConfig = new UserConfig(Role.USER, challengeConfig);

        UserConfigDTO userConfigResponse = userConfigMapper.toDTO(userConfig);

        assertEquals(0, userConfig.getRole().compareTo(Role.USER));
        assertEquals(challengeConfigDTO, userConfigResponse.getChallengeConfig());
    }
}
