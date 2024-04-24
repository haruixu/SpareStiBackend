package org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.ChallengeConfigDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.ChallengeTypeConfigDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.ChallengeConfig;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.ChallengeTypeConfig;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.Experience;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.Motivation;

public class ChallengeConfigMapperTest {

    @Mock private ChallengeTypeConfigMapper challengeTypeConfigMapper;

    @InjectMocks
    private ChallengeConfigMapper challengeConfigMapper = ChallengeConfigMapper.INSTANCE;

    private ChallengeTypeConfig challengeTypeConfig;
    private Set<ChallengeTypeConfig> challengeTypeConfigs;
    private Set<ChallengeTypeConfigDTO> challengeTypeConfigDTOs;
    private ChallengeTypeConfigDTO challengeTypeConfigDTO;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        challengeTypeConfig =
                new ChallengeTypeConfig("CLOTHING", new BigDecimal(100), new BigDecimal(1));
        challengeTypeConfigDTO =
                new ChallengeTypeConfigDTO("CLOTHING", new BigDecimal(100), new BigDecimal(1));

        challengeTypeConfigs = new HashSet<>();
        challengeTypeConfigs.add(challengeTypeConfig);

        challengeTypeConfigDTOs = new HashSet<>();
        challengeTypeConfigDTOs.add(challengeTypeConfigDTO);
    }

    @Test
    public void testToDTO() {

        when(challengeTypeConfigMapper.toDTO(challengeTypeConfig))
                .thenReturn(challengeTypeConfigDTO);

        ChallengeConfig challengeConfig =
                new ChallengeConfig(Experience.VERY_HIGH, Motivation.HIGH, challengeTypeConfigs);
        ChallengeConfigDTO challengeConfigDTO = challengeConfigMapper.toDTO(challengeConfig);

        assertEquals(0, challengeConfigDTO.experience().compareTo(Experience.VERY_HIGH));
        assertEquals(0, challengeConfigDTO.motivation().compareTo(Motivation.HIGH));
        assertEquals(challengeTypeConfigDTOs, challengeConfigDTO.challengeTypeConfigs());
    }

    @Test
    public void testToEntity() {

        when(challengeTypeConfigMapper.toEntity(challengeTypeConfigDTO))
                .thenReturn(challengeTypeConfig);

        ChallengeConfigDTO challengeConfigDTO =
                new ChallengeConfigDTO(
                        Experience.VERY_HIGH, Motivation.VERY_HIGH, challengeTypeConfigDTOs);
        ChallengeConfig challengeConfig = challengeConfigMapper.toEntity(challengeConfigDTO);

        assertEquals(0, challengeConfig.getExperience().compareTo(Experience.VERY_HIGH));
        assertEquals(0, challengeConfig.getMotivation().compareTo(Motivation.VERY_HIGH));
        assertEquals(challengeTypeConfigs, challengeConfig.getChallengeTypeConfigs());
    }
}
