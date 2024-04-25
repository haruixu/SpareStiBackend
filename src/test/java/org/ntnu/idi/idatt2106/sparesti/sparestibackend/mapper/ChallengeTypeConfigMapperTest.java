package org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.config.ChallengeTypeConfigDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper.config.ChallengeTypeConfigMapper;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.config.ChallengeTypeConfig;

public class ChallengeTypeConfigMapperTest {

    private final ChallengeTypeConfigMapper challengeTypeConfigMapper =
            ChallengeTypeConfigMapper.INSTANCE;

    @Test
    public void testToDTO() {
        ChallengeTypeConfig challengeTypeConfig =
                new ChallengeTypeConfig("TestType", BigDecimal.TEN, BigDecimal.ONE);

        ChallengeTypeConfigDTO challengeTypeConfigDTO =
                challengeTypeConfigMapper.toDTO(challengeTypeConfig);

        assertNotNull(challengeTypeConfigDTO);
        assertEquals("TestType", challengeTypeConfigDTO.type());
        assertEquals(BigDecimal.TEN, challengeTypeConfigDTO.generalAmount());
        assertEquals(BigDecimal.ONE, challengeTypeConfigDTO.specificAmount());
    }

    @Test
    public void testToEntity() {
        ChallengeTypeConfigDTO challengeTypeConfigDTO =
                new ChallengeTypeConfigDTO("TestType", BigDecimal.TEN, BigDecimal.ONE);

        ChallengeTypeConfig challengeTypeConfig =
                challengeTypeConfigMapper.toEntity(challengeTypeConfigDTO);

        assertNotNull(challengeTypeConfig);
        assertEquals("TestType", challengeTypeConfig.getType());
        assertEquals(BigDecimal.TEN, challengeTypeConfig.getGeneralAmount());
        assertEquals(BigDecimal.ONE, challengeTypeConfig.getSpecificAmount());
    }

    @Test
    public void testUpdateEntity() {
        ChallengeTypeConfig existingConfig =
                new ChallengeTypeConfig("TestType", BigDecimal.TEN, BigDecimal.ONE);
        ChallengeTypeConfigDTO updatedDTO =
                new ChallengeTypeConfigDTO(
                        "UpdatedType", BigDecimal.valueOf(20), BigDecimal.valueOf(2));

        ChallengeTypeConfig updatedConfig =
                challengeTypeConfigMapper.updateEntity(existingConfig, updatedDTO);

        assertNotNull(updatedConfig);
        assertEquals("TestType", updatedConfig.getType());
        assertEquals(BigDecimal.valueOf(20), updatedConfig.getGeneralAmount());
        assertEquals(BigDecimal.valueOf(2), updatedConfig.getSpecificAmount());
    }
}
