package org.ntnu.idi.idatt2106.sparesti.sparestibackend.validation.goal;

import java.util.List;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.goal.GoalCreateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.BadInputException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.goal.ActiveGoalLimitExceededException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.goal.NotActiveGoalException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.validation.ObjectValidator;
import org.springframework.stereotype.Component;

/**
 * Validator class for fields in GoalCreateDTO
 *
 * @author Harry L.X
 * @version 1.0
 * @since 29.4.24
 */
@Component
public class GoalValidator extends ObjectValidator<GoalCreateDTO> {

    /**
     * Validation method for dto field constraints. In addition, also checks
     * that saved amount cannot be greater than target amount.
     * @param dto Object of type T
     * @throws BadInputException If saved amount is greater than target
     */
    @Override
    public void validate(GoalCreateDTO dto) throws BadInputException {
        checkConstraints(dto);
        if (dto.saved().doubleValue() > dto.target().doubleValue()) {
            throw new BadInputException(
                    "Det du har spart kan ikke være større enn sparemålbeløpet");
        }
    }

    /**
     * Validates that list of goal Id's for priority sorting match the active goal Id's
     * @param distinctGoalIds Goal Id's with new sorting order
     * @param activeGoalIds Active goal Id's
     */
    public void validateGoalIds(List<Long> distinctGoalIds, List<Long> activeGoalIds) {
        for (Long goalId : distinctGoalIds) {
            if (!activeGoalIds.contains(goalId)) {
                throw new NotActiveGoalException(goalId);
            }
        }
        if (distinctGoalIds.size() > activeGoalIds.size()) {
            throw new ActiveGoalLimitExceededException();
        }
        if (distinctGoalIds.size() < activeGoalIds.size()) {
            throw new IllegalArgumentException(
                    "Størrelsen til prioritetslisten matcher ikke størrelsen listen med aktive"
                            + " mål");
        }
    }
}
