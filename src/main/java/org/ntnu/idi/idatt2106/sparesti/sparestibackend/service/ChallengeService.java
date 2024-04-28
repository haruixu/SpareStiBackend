package org.ntnu.idi.idatt2106.sparesti.sparestibackend.service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
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
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.repository.ChallengeRepository;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.validation.ChallengeValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeValidator<ChallengeUpdateDTO> updateChallengeValidator;
    private final ChallengeValidator<ChallengeCreateDTO> createChallengeValidator;

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
        Challenge challenge = findChallengeByIdAndUser(challengeId, user);

        if (!user.getId().equals(challenge.getUser().getId())) {
            throw new ChallengeNotFoundException(challengeId);
        }

        if (challenge.getCompletedOn() != null) {
            throw new ChallengeAlreadyCompletedException(challengeId);
        }

        challenge.setCompletedOn(ZonedDateTime.now());
        updateUserSavedAmount(user, challenge.getSaved().doubleValue());
        updateStreak(challenge);
        challengeRepository.save(challenge);
        return ChallengeMapper.INSTANCE.toDTO(challenge);
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

    private Challenge findChallengeByIdAndUser(Long challengeId, User user) {
        return challengeRepository
                .findByIdAndUser(challengeId, user)
                .orElseThrow(() -> new ChallengeNotFoundException(challengeId));
    }

    public ChallengeDTO save(ChallengeCreateDTO challengeCreateDTO, User user)
            throws ChallengeNotFoundException, ObjectNotValidException {
        createChallengeValidator.validate(challengeCreateDTO);
        Challenge newChallenge = ChallengeMapper.INSTANCE.toEntity(challengeCreateDTO, user);
        Challenge persistedChallenge = challengeRepository.save(newChallenge);
        return ChallengeMapper.INSTANCE.toDTO(persistedChallenge);
    }

    public ChallengeDTO updateChallenge(Long id, ChallengeUpdateDTO challengeUpdateDTO, User user)
            throws ChallengeNotFoundException, ObjectNotValidException {
        updateChallengeValidator.validate(challengeUpdateDTO);
        Challenge challenge = privateGetChallenge(id, user);
        Challenge updatedChallenge =
                ChallengeMapper.INSTANCE.updateEntity(challenge, challengeUpdateDTO);
        // TODO: if saved > target, complete challenge
        if (challenge.getSaved().doubleValue() >= challenge.getTarget().doubleValue()) {
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
                                        .map(challenge -> challenge.getType().toLowerCase())
                                        .toList()
                                        .contains(type.toLowerCase()))
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
        Random random = new Random();

        return availableTypes.stream()
                .map(type -> generateChallenge(user, type, motivationValue, random))
                .toList();
    }

    /**
     * Generates a single challenge based on a type from user config
     * The generated challenge introduces some level of randomization in
     * target value which is +/- 10% of the config type's general amount (amount spent in week)
     * Due date is also random, from 7-10 days.
     * @param user User who challenge is generated for
     * @param type Challenge type
     * @param motivationValue Willingness for saving
     * @param random Randomizer
     * @return Generated challenge of a given type
     */
    private ChallengeDTO generateChallenge(
            User user, String type, double motivationValue, Random random) {
        String title = type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase();
        double targetRandomizerValue = random.nextDouble(0.9, 1.1);
        int dayRandomizerValue = random.nextInt(0, 4);

        // Finding challenge type config from available types
        ChallengeTypeConfig challengeTypeConfig =
                user.getUserConfig().getChallengeConfig().getChallengeTypeConfigs().stream()
                        .filter(config -> config.getType().equalsIgnoreCase(type))
                        .findFirst()
                        .get();

        // Extract data from config type to be used later
        double generalAmount = challengeTypeConfig.getGeneralAmount().doubleValue();
        BigDecimal specificAmount = challengeTypeConfig.getSpecificAmount();

        // Save a fraction of general amount, based on level of motivation, rounded to nearest 10
        int targetValue =
                (int) Math.round(generalAmount / motivationValue * targetRandomizerValue / 10) * 10;
        BigDecimal target = new BigDecimal(targetValue);
        Challenge challenge =
                new Challenge(
                        null,
                        title,
                        BigDecimal.ZERO,
                        target,
                        specificAmount,
                        null,
                        null,
                        null,
                        ZonedDateTime.now().plusDays(7 + dayRandomizerValue),
                        type,
                        user,
                        BigDecimal.ZERO);
        Challenge savedChallenge = challengeRepository.save(challenge);
        return ChallengeMapper.INSTANCE.toDTO(savedChallenge);
    }
}
