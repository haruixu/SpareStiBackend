package org.ntnu.idi.idatt2106.sparesti.sparestibackend.service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.challenge.ChallengeCreateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.challenge.ChallengeDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.challenge.ChallengeUpdateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.ChallengeAlreadyCompletedException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.ChallengeConfigNotFoundException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.ChallengeNotFoundException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.validation.ObjectNotValidException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper.ChallengeMapper;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Challenge;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.ChallengeTypeConfig;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Goal;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.repository.ChallengeRepository;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.validation.ObjectValidator;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.validation.challenge.ChallengeValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ObjectValidator<ChallengeUpdateDTO> updateChallengeValidator;
    private final ChallengeValidator createChallengeValidator;
    private final GoalService goalService;

    public ChallengeDTO save(ChallengeCreateDTO challengeCreateDTO, User user)
            throws ChallengeNotFoundException, ObjectNotValidException {
        createChallengeValidator.validate(challengeCreateDTO);
        Challenge newChallenge = ChallengeMapper.INSTANCE.toEntity(challengeCreateDTO, user);

        Challenge persistedChallenge = challengeRepository.save(newChallenge);

        double increment = persistedChallenge.getSaved().doubleValue();
        if (increment > 0) {
            cascadeToGoal(user, increment);
        }

        if (persistedChallenge.getSaved().doubleValue()
                == persistedChallenge.getTarget().doubleValue()) {
            persistedChallenge.setCompletedOn(ZonedDateTime.now());
            updateUserSavedAmount(user, persistedChallenge.getSaved().doubleValue());

            updateStreak(persistedChallenge);
            Challenge completedChallenge = challengeRepository.save(persistedChallenge);
            return ChallengeMapper.INSTANCE.toDTO(completedChallenge);
        }
        return ChallengeMapper.INSTANCE.toDTO(persistedChallenge);
    }

    public ChallengeDTO updateChallenge(Long id, ChallengeUpdateDTO challengeUpdateDTO, User user)
            throws ChallengeNotFoundException, ObjectNotValidException {
        updateChallengeValidator.validate(challengeUpdateDTO);
        Challenge challenge = privateGetChallenge(id, user);
        if (challenge.getCompletedOn() != null) {
            throw new ChallengeAlreadyCompletedException(challenge.getId());
        }

        double increment =
                challengeUpdateDTO.saved().doubleValue() - challenge.getSaved().doubleValue();
        Challenge updatedChallenge =
                ChallengeMapper.INSTANCE.updateEntity(challenge, challengeUpdateDTO);

        if (increment > 0) {
            cascadeToGoal(user, increment);
        }

        if (updatedChallenge.getSaved().doubleValue()
                >= updatedChallenge.getTarget().doubleValue()) {
            return completeChallenge(updatedChallenge.getId(), user);
        }

        Challenge persistedChallenge = challengeRepository.save(updatedChallenge);
        return ChallengeMapper.INSTANCE.toDTO(persistedChallenge);
    }

    public ChallengeDTO getChallenge(Long challengeId, User user)
            throws ChallengeNotFoundException {
        return ChallengeMapper.INSTANCE.toDTO(privateGetChallenge(challengeId, user));
    }

    public void deleteChallenge(Long challengeId, User user) throws ChallengeNotFoundException {
        Challenge challenge = privateGetChallenge(challengeId, user);
        challengeRepository.delete(challenge);
    }

    private Challenge privateGetChallenge(Long challengeId, User user) {
        return challengeRepository
                .findByIdAndUser(challengeId, user)
                .orElseThrow(() -> new ChallengeNotFoundException(challengeId));
    }

    public Page<ChallengeDTO> getChallengesByUser(User user, Pageable pageable)
            throws ChallengeNotFoundException {
        return challengeRepository.findByUser(user, pageable).map(ChallengeMapper.INSTANCE::toDTO);
    }

    public Page<ChallengeDTO> getActiveChallenges(User user, Pageable pageable) {
        return challengeRepository
                .findAllByCompletedOnIsNullAndUser(user, pageable)
                .map(ChallengeMapper.INSTANCE::toDTO);
    }

    public Page<ChallengeDTO> getCompletedChallenges(User user, Pageable pageable) {
        return challengeRepository
                .findAllByCompletedOnIsNotNullAndUser(user, pageable)
                .map(ChallengeMapper.INSTANCE::toDTO);
    }

    public ChallengeDTO completeChallenge(Long challengeId, User user) {
        Challenge challenge = privateGetChallenge(challengeId, user);

        if (challenge.getCompletedOn() != null) {
            throw new ChallengeAlreadyCompletedException(challengeId);
        }

        challenge.setCompletedOn(ZonedDateTime.now());
        updateUserSavedAmount(user, challenge.getSaved().doubleValue());

        updateStreak(challenge);
        Challenge completedChallenge = challengeRepository.save(challenge);
        return ChallengeMapper.INSTANCE.toDTO(completedChallenge);
    }

    private void cascadeToGoal(User user, double increment) {
        while (increment > 0) {
            Optional<Goal> optionalGoal =
                    user.getGoals().stream()
                            .filter(goal -> goal.getCompletedOn() == null)
                            .findFirst();
            // No goals to cascace onto
            if (optionalGoal.isEmpty()) {
                return;
            }

            Goal goal = optionalGoal.get();
            assert goal.getPriority() == 1;
            double goalDifference = goal.getTarget().doubleValue() - goal.getSaved().doubleValue();
            // Set saved value to original value + overflow
            if (increment < goalDifference) {
                goal.setSaved(BigDecimal.valueOf(goal.getSaved().doubleValue() + increment));
            }
            // if overflow >= goalDifference, complete goal
            else {
                goal.setSaved(goal.getTarget());
                goalService.completeGoal(goal.getId(), user);
            }

            increment = increment - goalDifference;
        }
    }

    private void updateStreak(Challenge challenge) {
        User user = challenge.getUser();

        boolean resetStreak =
                challenge.getCompletedOn().isAfter(challenge.getDue())
                        || user.getChallenges().stream()
                                .anyMatch(
                                        _challenge ->
                                                _challenge.getDue() != null
                                                        && _challenge.getCompletedOn() == null
                                                        && _challenge
                                                                .getDue()
                                                                .isBefore(ZonedDateTime.now()));

        if (resetStreak) {
            user.setStreak(0L);
            user.setStreakStart(null);
        } else {
            user.setStreak(user.getStreak() + 1);
            if (user.getStreakStart() == null) {
                user.setStreakStart(ZonedDateTime.now());
            }
        }
    }

    private void updateUserSavedAmount(User user, double increment) {
        user.setSavedAmount(BigDecimal.valueOf(user.getSavedAmount().doubleValue() + increment));
    }

    /**
     * Gets list of generated challenges
     * @param user The user who challenges are generated for.
     * @return List of generated challenges
     */
    public List<ChallengeDTO> getGeneratedChallenges(User user) {
        if (user.getUserConfig().getChallengeConfig().getMotivation() == null) {
            throw new ChallengeConfigNotFoundException(user.getId());
        }
        List<String> types = getAvailableTypes(user);
        return generateChallenges(types, user);
    }

    /**
     * Gets the types that challenges can be generated for. An available type is a type
     * defined in the user config that currently has no active challenges.
     * @param user The user that the config is based on.
     * @return List of types as strings
     */
    private List<String> getAvailableTypes(User user) {
        return user.getUserConfig().getChallengeConfig().getChallengeTypeConfigs().stream()
                .map(ChallengeTypeConfig::getType)
                .distinct()
                .filter(
                        type ->
                                !challengeRepository
                                        .findAllByCompletedOnIsNullAndUser(user)
                                        .stream()
                                        .map(Challenge::getType)
                                        .filter(Objects::nonNull)
                                        .toList()
                                        .contains(type))
                .toList();
    }

    /**
     * Generates a list of randomized challenges. The randomization is in the target amount
     * and due date
     * @param availableTypes Types that challenges are generated for.
     * @param user The user who challenges is generated for.
     * @return List of generated challenges with slight randomization.
     */
    private List<ChallengeDTO> generateChallenges(List<String> availableTypes, User user) {
        double motivationValue = user.getUserConfig().getChallengeConfig().getMotivation().getVal();

        return availableTypes.stream()
                .map(type -> generateChallenge(user, type, motivationValue))
                .filter(Objects::nonNull)
                .toList();
    }

    /**
     * Generates a single challenge based on a type from user config
     * @param user User who challenge is generated for
     * @param type Challenge type
     * @param motivationValue Willingness for saving
     * @return Generated challenge of a given type
     */
    private ChallengeDTO generateChallenge(User user, String type, double motivationValue) {

        // Finding challenge type config from available types
        ChallengeTypeConfig challengeTypeConfig =
                user.getUserConfig().getChallengeConfig().getChallengeTypeConfigs().stream()
                        .filter(config -> config.getType().equalsIgnoreCase(type))
                        .findFirst()
                        .get();

        double amountPerWeek = challengeTypeConfig.getGeneralAmount().doubleValue();
        double amountPerUnit = challengeTypeConfig.getSpecificAmount().doubleValue();

        int weeks = calculateWeeks(amountPerWeek, amountPerUnit);
        double units = weeks * amountPerWeek / amountPerUnit;

        if (units * motivationValue < 1) {
            // Throw away challenges with unrealistic target value
            return null;
        } else {
            double targetValue = Math.round(units * motivationValue) * amountPerUnit;
            String title = type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase();
            return createChallenge(
                    title, amountPerUnit, amountPerWeek, weeks, targetValue, type, user);
        }
    }

    private ChallengeDTO createChallenge(
            String title,
            double amountPerUnit,
            double amountPerWeek,
            int weeks,
            double targetValue,
            String type,
            User user) {
        double unitsPerWeek = amountPerWeek / amountPerUnit;
        double targetUnits = targetValue / amountPerUnit;
        String descriptionSuffix = weeks == 1 ? "den neste uka." : "de neste " + weeks + " ukene.";
        String description =
                String.format(
                                "Du bruker %.2fkr hver du kjøper denne typen. Per uke bruker du i"
                                        + " snitt %.2fkr (%.1f enheter). Din utfordring vil være å"
                                        + " spare %.2fkr (%.1f enheter) ",
                                amountPerUnit,
                                amountPerWeek,
                                unitsPerWeek,
                                targetValue,
                                targetUnits)
                        + descriptionSuffix;

        BigDecimal target = new BigDecimal(targetValue);
        BigDecimal perPurchase = new BigDecimal(amountPerUnit);
        long days = weeks * 7L;
        Challenge challenge =
                new Challenge(
                        null,
                        title,
                        BigDecimal.ZERO,
                        target,
                        perPurchase,
                        description,
                        null,
                        null,
                        ZonedDateTime.now().plusDays(days),
                        type,
                        user,
                        BigDecimal.ZERO);
        return ChallengeMapper.INSTANCE.toDTO(challenge);
    }

    private int calculateWeeks(double amountPerWeek, double amountPerUnit) {
        // In case amountPerWeek < amountPerUnit, e.g use 1000kr each shopping trip but use 500kr
        // each week
        for (int week = 1; week <= 4; week++) {
            double units = week * amountPerWeek / amountPerUnit;
            if (units >= 1) {
                return week;
            }
        }
        return 0;
    }
}
