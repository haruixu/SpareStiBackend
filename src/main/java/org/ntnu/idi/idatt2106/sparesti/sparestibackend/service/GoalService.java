package org.ntnu.idi.idatt2106.sparesti.sparestibackend.service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.goal.GoalCreateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.goal.GoalResponseDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.goal.GoalUpdateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.goal.ActiveGoalLimitExceededException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.goal.GoalNotFoundException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.validation.ObjectNotValidException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper.GoalMapper;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Goal;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.repository.GoalRepository;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.validation.ObjectValidator;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.validation.goal.GoalValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service class for handling business logic related to Goal entity and DTO's
 *
 * @author Harry X.
 * @version 1.0
 * @since 22.4.2024
 */
@Service
@RequiredArgsConstructor
public class GoalService {

    private final GoalRepository goalRepository;

    private static final int ACTIVE_GOAL_LIMIT = 10;

    private final ObjectValidator<GoalUpdateDTO> updateValidator;
    private final GoalValidator createValidator;

    /**
     * Finds a user's saving goal specified by its ID
     * @param id Identifies goal
     * @param user User that owns the goal
     * @return Mapped GoalDTO from the Goal entity
     */
    public GoalResponseDTO findUserGoal(Long id, User user) {
        return GoalMapper.INSTANCE.toDTO(findGoalByIdAndUser(id, user));
    }

    /**
     * Gets a page of a user's saving goals
     * @param user User that owns the goals
     * @param pageable Pageable object that configures the page dimensions
     * @return Page of goals
     */
    public Page<GoalResponseDTO> getUserGoals(User user, Pageable pageable) {
        return goalRepository.findAllByUser(user, pageable).map(GoalMapper.INSTANCE::toDTO);
    }

    /**
     * Saves a goal entity.
     * @param goalDTO Goal info that is to be saved
     * @param user User that owns the goal
     * @return Goal info of the saved goal
     * @throws ObjectNotValidException If the input goal info fields are invalid
     * @throws ActiveGoalLimitExceededException If there already are 10 active goals for the user
     */
    public GoalResponseDTO save(GoalCreateDTO goalDTO, User user)
            throws ObjectNotValidException, ActiveGoalLimitExceededException {
        createValidator.validate(goalDTO);
        Goal goal = GoalMapper.INSTANCE.toEntity(goalDTO, user);
        if (goal.getSaved().doubleValue() >= goal.getTarget().doubleValue()) {
            goal.setCompletedOn(ZonedDateTime.now());
            goal.setPriority(ACTIVE_GOAL_LIMIT + 1L);
            return GoalMapper.INSTANCE.toDTO(goalRepository.save(goal));
        }
        long priority = getDefaultPriority(user);

        if (priority > ACTIVE_GOAL_LIMIT) throw new ActiveGoalLimitExceededException();

        goal.setPriority(priority);

        return GoalMapper.INSTANCE.toDTO(goalRepository.save(goal));
    }

    /**
     * Assigns a goal a default priority which is the active goal list size
     * @param user User that owns the goal
     * @return Goal priority value
     */
    private long getDefaultPriority(User user) {
        return getActiveUserGoals(user).size() + 1;
    }

    /**
     * Updates a goal of a user, specified by its ID
     * @param id Identifies a goal
     * @param goalDTO New goal info
     * @param user user that owns the goal
     * @return Updated goal info
     * @throws ObjectNotValidException If the input goal DTO fields are invalid
     */
    public GoalResponseDTO update(Long id, GoalUpdateDTO goalDTO, User user)
            throws ObjectNotValidException {
        updateValidator.validate(goalDTO);
        Goal currentGoal = findGoalByIdAndUser(id, user);
        Goal updatedGoal = GoalMapper.INSTANCE.updateEntity(currentGoal, goalDTO);
        if (updatedGoal.getSaved().doubleValue() >= updatedGoal.getTarget().doubleValue()) {
            completeGoal(updatedGoal.getId(), user);
        }
        return GoalMapper.INSTANCE.toDTO(goalRepository.save(updatedGoal));
    }

    /**
     * Helper method for finding goal of a user, specified by ID
     * @param id Identifies a goal
     * @param user User that owns the goal
     * @return The goal with the matching ID that also belongs to the user
     */
    private Goal findGoalByIdAndUser(Long id, User user) {
        return goalRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() -> new GoalNotFoundException(id));
    }

    /**
     * Gets a list of active user goals
     * @param user User that owns the goals
     * @return List of active user goals
     */
    public List<GoalResponseDTO> getActiveUserGoals(User user) {
        return goalRepository.findAllByCompletedOnIsNullAndUser(user).stream()
                .map(GoalMapper.INSTANCE::toDTO)
                .toList();
    }

    /**
     * Gets a page of completed user goals
     * @param user User that owns the goals
     * @param pageable Pageable object that configures the page dimensions
     * @return Page of a user's completed goals
     */
    public Page<GoalResponseDTO> getCompletedUserGoals(User user, Pageable pageable) {
        return goalRepository
                .findAllByCompletedOnIsNotNullAndUser(user, pageable)
                .map(GoalMapper.INSTANCE::toDTO);
    }

    /**
     * Completes a goal of a user, specified by its ID
     * @param goalId Identifies the goal
     * @param user User that owns the goal
     * @return Completed goal info
     */
    public GoalResponseDTO completeGoal(Long goalId, User user) {
        Goal completedGoal = setCompleted(goalId, user);
        setNewPriorities(user);
        return GoalMapper.INSTANCE.toDTO(completedGoal);
    }

    /**
     * Sets the completedOn attribute to a date and the
     * priority of a goal to 11
     * @param goalId Identifies the target goal
     * @param user user that owns the goal
     * @return The completed goal
     */
    private Goal setCompleted(Long goalId, User user) {
        Goal goal = findGoalByIdAndUser(goalId, user);
        if (findGoalByIdAndUser(goalId, user).getCompletedOn() == null) {
            goal.setCompletedOn(ZonedDateTime.now());
            goal.setPriority((long) ACTIVE_GOAL_LIMIT + 1);
        }
        return goalRepository.save(goal);
    }

    /**
     * Updates the priority ordering of the list of active saving goals of a user
     * @param goalIds Ordered list of goal Id's, representing the new order
     * @param user User that owns the goals
     * @return List of goals with the new priorities
     */
    public List<GoalResponseDTO> updatePriorities(List<Long> goalIds, User user) {
        List<Long> distinctGoalIds = goalIds.stream().distinct().toList();
        List<Long> activeGoalsIds =
                goalRepository.findAllByCompletedOnIsNullAndUser(user).stream()
                        .map(Goal::getId)
                        .toList();
        createValidator.validateGoalIds(distinctGoalIds, activeGoalsIds);

        List<Goal> updatedGoalsList = new ArrayList<>();
        for (int i = 0; i < distinctGoalIds.size(); i++) {
            Goal goal = findGoalByIdAndUser(distinctGoalIds.get(i), user);
            int priority = i + 1;
            goal.setPriority((long) priority);
            updatedGoalsList.add(goal);
        }

        return goalRepository.saveAll(updatedGoalsList).stream()
                .map(GoalMapper.INSTANCE::toDTO)
                .toList();
    }

    /**
     * Deletes a goal of a user, specified by its ID
     * @param id Identifies a goal
     * @param user User that owns the goal
     */
    public void deleteUserGoal(Long id, User user) {
        goalRepository.deleteByIdAndUser(id, user);
        setNewPriorities(user);
    }

    /**
     * Sets new priorities for goals
     * @param user User that owns the goals
     */
    private void setNewPriorities(User user) {
        List<Goal> activeGoals =
                goalRepository.findAllByCompletedOnIsNullAndUserOrderByPriorityAsc(user);
        for (int i = 0; i < activeGoals.size(); i++) {
            long priority = (long) i + 1;
            activeGoals.get(i).setPriority(priority);
        }
        goalRepository.saveAll(activeGoals);
    }
}
