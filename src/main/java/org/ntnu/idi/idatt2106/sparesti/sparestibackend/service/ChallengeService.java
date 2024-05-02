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

/**
 * Service class for managing challenges. Provides functionality for
 * creating, updating, retrieving, generating and deleting challenges,
 * as well as managing challenge-related
 * operations such as cascading saves to goals and updating user stats.
 *
 * @author Y.A. Marouga, H.L Xu and L.M.L Nilsen
 * @Service Annotation that marks this class as a Spring service, denoting it as a business service facet.
 * @RequiredArgsConstructor Lombok annotation that generates a constructor requiring arguments for final fields.
 */
@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ObjectValidator<ChallengeUpdateDTO> updateChallengeValidator;
    private final ChallengeValidator createChallengeValidator;
    private final GoalService goalService;

    /**
     * Creates and persists a new challenge based on provided DTO and user details.
     * Calculates initial savings impact and completes the challenge if the saved amount meets the target.
     *
     * @param challengeCreateDTO DTO containing challenge creation details.
     * @param user The user creating the challenge.
     * @return A ChallengeDTO representing the newly created challenge.
     * @throws ChallengeNotFoundException if the challenge configuration is not found.
     * @throws ObjectNotValidException if the challenge details are not valid as per validation constraints.
     */
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

    /**
     * Updates an existing challenge with new data from a provided DTO if the challenge has not been completed.
     *
     * @param id The ID of the challenge to update.
     * @param challengeUpdateDTO DTO containing update details.
     * @param user The user associated with the challenge.
     * @return Updated ChallengeDTO.
     * @throws ChallengeNotFoundException if the challenge cannot be found.
     * @throws ObjectNotValidException if the update details are not valid.
     * @throws ChallengeAlreadyCompletedException if the challenge is already completed.
     */
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

    /**
     * Retrieves a specific challenge by ID and user.
     *
     * @param challengeId The ID of the challenge.
     * @param user The user associated with the challenge.
     * @return ChallengeDTO of the retrieved challenge.
     * @throws ChallengeNotFoundException if no such challenge exists.
     */
    public ChallengeDTO getChallenge(Long challengeId, User user)
            throws ChallengeNotFoundException {
        return ChallengeMapper.INSTANCE.toDTO(privateGetChallenge(challengeId, user));
    }

    /**
     * Deletes a challenge by ID and user, ensuring it is no longer present in the database.
     *
     * @param challengeId The ID of the challenge to delete.
     * @param user The user associated with the challenge.
     * @throws ChallengeNotFoundException if no such challenge exists to delete.
     */
    public void deleteChallenge(Long challengeId, User user) throws ChallengeNotFoundException {
        Challenge challenge = privateGetChallenge(challengeId, user);
        challengeRepository.delete(challenge);
    }

    /**
     * Internal method to retrieve a challenge, ensuring it exists and belongs to the specified user.
     *
     * @param challengeId The ID of the challenge.
     * @param user The user to whom the challenge must belong.
     * @return The found challenge.
     * @throws ChallengeNotFoundException if no such challenge is found.
     */
    private Challenge privateGetChallenge(Long challengeId, User user) {
        return challengeRepository
                .findByIdAndUser(challengeId, user)
                .orElseThrow(() -> new ChallengeNotFoundException(challengeId));
    }

    /**
     * Retrieves all challenges associated with a specific user and paginates the results.
     *
     * @param user The user whose challenges are to be retrieved.
     * @param pageable Pagination and sorting details.
     * @return A page of ChallengeDTOs representing the user's challenges.
     * @throws ChallengeNotFoundException If no challenges are found for the user, this exception may be thrown.
     */
    public Page<ChallengeDTO> getChallengesByUser(User user, Pageable pageable)
            throws ChallengeNotFoundException {
        return challengeRepository.findByUser(user, pageable).map(ChallengeMapper.INSTANCE::toDTO);
    }

    /**
     * Retrieves all active (not completed) challenges for a specific user and paginates the results.
     *
     * @param user The user whose active challenges are to be retrieved.
     * @param pageable Pagination and sorting details.
     * @return A page of ChallengeDTOs of active challenges.
     */
    public Page<ChallengeDTO> getActiveChallenges(User user, Pageable pageable) {
        return challengeRepository
                .findAllByCompletedOnIsNullAndUser(user, pageable)
                .map(ChallengeMapper.INSTANCE::toDTO);
    }

    /**
     * Retrieves all completed challenges for a specific user and paginates the results.
     *
     * @param user The user whose completed challenges are to be retrieved.
     * @param pageable Pagination and sorting details.
     * @return A page of ChallengeDTOs of completed challenges.
     */
    public Page<ChallengeDTO> getCompletedChallenges(User user, Pageable pageable) {
        return challengeRepository
                .findAllByCompletedOnIsNotNullAndUser(user, pageable)
                .map(ChallengeMapper.INSTANCE::toDTO);
    }

    /**
     * Marks a challenge as completed, updates user statistics such as saved amounts and streaks,
     * and persists these changes to the database.
     *
     * @param challengeId The ID of the challenge to complete.
     * @param user The user completing the challenge.
     * @return ChallengeDTO representing the completed challenge.
     * @throws ChallengeAlreadyCompletedException If the challenge is already marked as completed.
     */
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

    /**
     * Applies saved amount increments to goals in a cascading manner, ensuring that contributions
     * are allocated to goals according to their priority until all funds are exhausted.
     *
     * @param user The user whose goals are to be updated.
     * @param increment The amount by which the user's saved funds have increased.
     */
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

    /**
     * Updates the user's streak based on challenge completion relative to its due date and other active challenges.
     *
     * @param challenge The challenge being used to update the streak.
     */
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

    /**
     * Updates the total amount saved by the user based on new challenge contributions.
     *
     * @param user The user whose saved amount is being updated.
     * @param increment The amount added to the user's saved total.
     */
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

    /**
     * Creates a challenge object and maps it to a ChallengeDTO. This method calculates the
     * necessary parameters for a challenge based on provided financial goals and the temporal
     * scope specified by the user.
     *
     * @param title The title of the challenge.
     * @param amountPerUnit The amount spent per unit involved in the challenge (e.g., per purchase).
     * @param amountPerWeek The total amount spent per week on this type of challenge.
     * @param weeks The duration of the challenge, in weeks.
     * @param targetValue The financial target value that the challenge aims to achieve.
     * @param type The type of challenge, which could be a category or specific tag related to the user's activities.
     * @param user The user who is initiating the challenge.
     * @return A ChallengeDTO that encapsulates the newly created challenge's details.
     */
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
