package org.ntnu.idi.idatt2106.sparesti.sparestibackend.validation.goal;

import java.util.List;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.goal.GoalCreateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.BadInputException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.goal.ActiveGoalLimitExceededException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.goal.NotActiveGoalException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.validation.ObjectValidator;
import org.springframework.stereotype.Component;

@Component
public class GoalValidator extends ObjectValidator<GoalCreateDTO> {

    @Override
    public void validate(GoalCreateDTO dto) throws BadInputException {
        checkConstraints(dto);
        if (dto.saved().doubleValue() > dto.target().doubleValue()) {
            throw new BadInputException(
                    "Det du har spart kan ikke være større enn sparemålbeløpet");
        }
    }

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
