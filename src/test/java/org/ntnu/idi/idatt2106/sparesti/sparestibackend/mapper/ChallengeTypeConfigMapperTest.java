package org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.config.ChallengeTypeConfigDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.ChallengeTypeConfig;

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
        assertEquals("Testtype", challengeTypeConfig.getType());
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

    @Test
    public void testToDTOAndEntityWithNull() {
        assertNull(challengeTypeConfigMapper.toDTO(null));
        assertNull(challengeTypeConfigMapper.toEntity(null));
    }

    @Test
    public void testUpdateEntityWithNull() {
        ChallengeTypeConfig existingConfig =
                new ChallengeTypeConfig("TestType", BigDecimal.TEN, BigDecimal.ONE);

        ChallengeTypeConfig updatedConfig =
                challengeTypeConfigMapper.updateEntity(existingConfig, null);
        assertNotNull(updatedConfig);
        assertEquals(existingConfig.getType(), updatedConfig.getType());
        assertEquals(existingConfig.getGeneralAmount(), updatedConfig.getGeneralAmount());
        assertEquals(existingConfig.getSpecificAmount(), updatedConfig.getSpecificAmount());
    }
}
