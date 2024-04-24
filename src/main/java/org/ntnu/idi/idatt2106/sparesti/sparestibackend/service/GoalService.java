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
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.goal.NotActiveGoalException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper.GoalMapper;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Goal;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.repository.GoalRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoalService {

    private final GoalRepository goalRepository;

    private static final int ACTIVE_GOAL_LIMIT = 10;

    public GoalResponseDTO findUserGoal(Long id, User user) {
        return GoalMapper.INSTANCE.toDTO(findGoalByIdAndUser(id, user));
    }

    public Page<GoalResponseDTO> getUserGoals(User user, Pageable pageable) {
        return goalRepository.findAllByUser(user, pageable).map(GoalMapper.INSTANCE::toDTO);
    }

    public GoalResponseDTO save(GoalCreateDTO goalDTO, User user) {
        Goal goal = GoalMapper.INSTANCE.toEntity(goalDTO, user);
        long priority = getDefaultPriority(user);

        if (priority > ACTIVE_GOAL_LIMIT) throw new ActiveGoalLimitExceededException();

        goal.setPriority(priority);

        return GoalMapper.INSTANCE.toDTO(goalRepository.save(goal));
    }

    private long getDefaultPriority(User user) {
        return user.getGoals().size() + 1;
    }

    public GoalResponseDTO update(Long id, GoalUpdateDTO goalDTO, User user) {
        Goal currentGoal = findGoalByIdAndUser(id, user);
        Goal updatedGoal = GoalMapper.INSTANCE.updateEntity(currentGoal, goalDTO);
        return GoalMapper.INSTANCE.toDTO(goalRepository.save(updatedGoal));
    }

    /**
     * Helper method
     * @param id
     * @param user
     * @return
     */
    private Goal findGoalByIdAndUser(Long id, User user) {
        return goalRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() -> new GoalNotFoundException(id));
    }

    public List<GoalResponseDTO> getActiveUserGoals(User user) {
        return goalRepository.findAllByCompletedOnIsNullAndUser(user).stream()
                .map(GoalMapper.INSTANCE::toDTO)
                .toList();
    }

    public Page<GoalResponseDTO> getCompletedUserGoals(User user, Pageable pageable) {
        return goalRepository
                .findAllByCompletedOnIsNotNullAndUser(user, pageable)
                .map(GoalMapper.INSTANCE::toDTO);
    }

    public GoalResponseDTO completeGoal(Long goalId, User user) {
        Goal completedGoal = setCompleted(goalId, user);
        setNewPriorities(user);
        return GoalMapper.INSTANCE.toDTO(completedGoal);
    }

    private Goal setCompleted(Long goalId, User user) {
        Goal goal = findGoalByIdAndUser(goalId, user);
        if (findGoalByIdAndUser(goalId, user).getCompletedOn() == null) {
            goal.setCompletedOn(ZonedDateTime.now());
            goal.setPriority((long) ACTIVE_GOAL_LIMIT + 1);
        }
        return goalRepository.save(goal);
    }

    public List<GoalResponseDTO> updatePriorities(List<Long> goalIds, User user) {
        List<Long> distinctGoalIds = goalIds.stream().distinct().toList();
        validateGoalIds(distinctGoalIds, user);

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

    private void validateGoalIds(List<Long> goalIds, User user) {
        List<Long> activeGoalsIds =
                goalRepository.findAllByCompletedOnIsNullAndUser(user).stream()
                        .map(Goal::getId)
                        .toList();
        for (Long goalId : goalIds) {
            if (!activeGoalsIds.contains(goalId)) {
                throw new NotActiveGoalException(goalId);
            }
        }

        if (goalIds.size() > activeGoalsIds.size()) {
            throw new ActiveGoalLimitExceededException();
        }
        if (goalIds.size() < activeGoalsIds.size()) {
            throw new IllegalArgumentException(
                    "Size of priority list does not match size of active goals");
        }
    }

    public void deleteUserGoal(Long id, User user) {
        goalRepository.deleteByIdAndUser(id, user);
        setNewPriorities(user);
    }

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
