package org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.yubico.webauthn.data.ByteArray;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.challenge.ChallengeCreateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.challenge.ChallengeDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.challenge.ChallengeUpdateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Account;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Challenge;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.UserConfig;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.Role;

public class ChallengeMapperTest {

    private final ChallengeMapper challengeMapper = ChallengeMapper.INSTANCE;
    private User user;

    @BeforeEach
    public void setUp() {
        user =
                new User(
                        1L,
                        "test",
                        "user",
                        "username",
                        "password",
                        "test@mail.com",
                        null,
                        0L,
                        BigDecimal.ZERO,
                        new UserConfig(Role.USER, null),
                        new Account(),
                        new Account(),
                        new ByteArray(new byte[0]));
    }

    @Test
    public void testToDTO() {
        Challenge challenge = createSampleChallenge();
        ChallengeDTO challengeDTO = challengeMapper.toDTO(challenge);
        assertNotNull(challengeDTO);
        assertEquals(challenge.getTitle(), challengeDTO.title());
        assertEquals(challenge.getDescription(), challengeDTO.description());
        assertEquals(challenge.getSaved(), challengeDTO.saved());
        assertEquals(challenge.getDue(), challengeDTO.due());
        assertEquals(challenge.getTarget(), challengeDTO.target());
        assertEquals(challenge.getPerPurchase(), challengeDTO.perPurchase());
        assertEquals(challenge.getType(), challengeDTO.type());
        assertEquals(new BigDecimal("10.000"), challengeDTO.completion());
        assertEquals(challenge.getId(), challengeDTO.id());
        assertEquals(challenge.getCompletedOn(), challengeDTO.completedOn());
        assertEquals(challenge.getCreatedOn(), challengeDTO.createdOn());
    }

    @Test
    public void testToEntity() {
        ChallengeCreateDTO createDTO = createSampleCreateDTO();
        Challenge challenge = challengeMapper.toEntity(createDTO, user);
        assertNotNull(challenge);
        assertEquals(createDTO.title(), challenge.getTitle());
        assertEquals(createDTO.description(), challenge.getDescription());
        assertEquals(createDTO.saved(), challenge.getSaved());
        assertEquals(createDTO.due(), challenge.getDue());
        assertEquals(createDTO.perPurchase(), challenge.getPerPurchase());
        assertEquals(
                createDTO.type().substring(0, 1).toUpperCase()
                        + createDTO.type().substring(1).toLowerCase(),
                challenge.getType());
        assertEquals(createDTO.target(), challenge.getTarget());
        assertEquals(user, challenge.getUser());
        assertEquals(new BigDecimal("10.000"), challenge.getCompletion());
        assertNull(challenge.getCompletedOn());
        assertNull(challenge.getId());
    }

    @Test
    public void testUpdateEntity() {
        Challenge challenge = createSampleChallenge();
        ChallengeUpdateDTO updateDTO = createSampleUpdateDTO();
        Challenge updatedChallenge = challengeMapper.updateEntity(challenge, updateDTO);
        assertNotNull(updatedChallenge);
        assertEquals(updateDTO.title(), updatedChallenge.getTitle());
        assertEquals(updateDTO.description(), updatedChallenge.getDescription());
        assertEquals(updateDTO.saved(), updatedChallenge.getSaved());
        assertEquals(updateDTO.due(), updatedChallenge.getDue());
        assertEquals(updateDTO.target(), updatedChallenge.getTarget());
        assertEquals(updateDTO.perPurchase(), updatedChallenge.getPerPurchase());
        assertEquals(
                updateDTO.type().substring(0, 1).toUpperCase()
                        + updateDTO.type().substring(1).toLowerCase(),
                challenge.getType());
    }

    private Challenge createSampleChallenge() {
        return new Challenge(
                1L,
                "Sample Challenge",
                BigDecimal.ONE,
                BigDecimal.TEN,
                BigDecimal.ONE,
                "Sample Description",
                ZonedDateTime.now(),
                null,
                ZonedDateTime.now().plusDays(7),
                "Sample Type",
                null,
                BigDecimal.ZERO);
    }

    private ChallengeCreateDTO createSampleCreateDTO() {
        return new ChallengeCreateDTO(
                "Sample Challenge",
                BigDecimal.ONE,
                BigDecimal.TEN,
                BigDecimal.ONE,
                "Sample Description",
                ZonedDateTime.now().plusDays(7),
                "Sample Type");
    }

    private ChallengeUpdateDTO createSampleUpdateDTO() {
        return new ChallengeUpdateDTO(
                "Updated Challenge",
                BigDecimal.TEN,
                BigDecimal.valueOf(20),
                BigDecimal.valueOf(2),
                "Updated Description",
                ZonedDateTime.now().plusDays(14),
                "Updated Type");
    }
}
