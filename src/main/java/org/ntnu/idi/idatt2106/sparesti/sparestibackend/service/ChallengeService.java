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
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.validation.ObjectValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ObjectValidator<ChallengeCreateDTO> createChallengeValidator;
    private final ObjectValidator<ChallengeUpdateDTO> updateChallengeValidator;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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

    public List<ChallengeDTO> getGeneratedChallenges(User user) {
        logger.info("challenge config: {}", user.getUserConfig().getChallengeConfig());
        if (user.getUserConfig().getChallengeConfig().getMotivation() == null) {
            throw new ChallengeConfigNotFoundException(user.getId());
        }
        List<String> types =
                user.getUserConfig().getChallengeConfig().getChallengeTypeConfigs().stream()
                        .map(ChallengeTypeConfig::getType)
                        .distinct()
                        .filter(
                                type ->
                                        !challengeRepository
                                                .findAllByCompletedOnIsNullAndUser(user)
                                                .stream()
                                                .map(Challenge::getType)
                                                .toList()
                                                .contains(type))
                        .toList();
        logger.info("Available types: {}", types);
        return generateChallenges(types, user);
    }

    private List<ChallengeDTO> generateChallenges(List<String> availableTypes, User user) {
        double motivationValue = user.getUserConfig().getChallengeConfig().getMotivation().getVal();
        logger.info("Motivation value: {}", motivationValue);
        Random random = new Random();

        List<ChallengeDTO> generated =
                availableTypes.stream()
                        .map(
                                type -> {
                                    String title =
                                            type.substring(0, 1).toUpperCase()
                                                    + type.substring(1).toLowerCase();
                                    logger.info("title: {}", title);
                                    double targetRandomizerValue = random.nextDouble(0.9, 1.1);
                                    logger.info(
                                            "target randomizer value: {}", targetRandomizerValue);
                                    int dayRandomizerValue = random.nextInt(0, 4);
                                    logger.info("day randomizer value: {}", dayRandomizerValue);

                                    // TODO: If user can create own type? => Then
                                    ChallengeTypeConfig challengeTypeConfig =
                                            user
                                                    .getUserConfig()
                                                    .getChallengeConfig()
                                                    .getChallengeTypeConfigs()
                                                    .stream()
                                                    .filter(config -> config.getType().equals(type))
                                                    .findFirst()
                                                    .get();
                                    double generalAmount =
                                            challengeTypeConfig.getGeneralAmount().doubleValue();
                                    BigDecimal specificAmount =
                                            challengeTypeConfig.getSpecificAmount();

                                    // .map(config -> config.getGeneralAmount().intValue())
                                    // .reduce(0, Integer::sum);
                                    logger.info("General amount: {}", generalAmount);
                                    // Save a fraction of general amount, based on level of
                                    // motivation
                                    int targetValue =
                                            (int)
                                                            Math.round(
                                                                    generalAmount
                                                                            / motivationValue
                                                                            * targetRandomizerValue
                                                                            / 10)
                                                    * 10;
                                    logger.info("Target value: {}", targetValue);
                                    BigDecimal target = new BigDecimal(targetValue);
                                    Challenge challenge =
                                            new Challenge(
                                                    null,
                                                    title,
                                                    new BigDecimal(0),
                                                    target,
                                                    specificAmount,
                                                    null,
                                                    null,
                                                    null,
                                                    ZonedDateTime.now()
                                                            .plusDays(7 + dayRandomizerValue),
                                                    type,
                                                    user,
                                                    new BigDecimal(0));
                                    Challenge savedChallenge = challengeRepository.save(challenge);
                                    return ChallengeMapper.INSTANCE.toDTO(savedChallenge);
                                })
                        .toList();
        return generated;
    }
}
