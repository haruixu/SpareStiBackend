package org.ntnu.idi.idatt2106.sparesti.sparestibackend.validation;

import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.challenge.ChallengeCreateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.challenge.ChallengeUpdateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.BadInputException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("challengeValidator")
public class ChallengeValidator<T> extends ObjectValidator<T> {

    @Override
    public void validate(T object) {
        checkConstraints(object);
        if (object instanceof ChallengeUpdateDTO) {
            if (((ChallengeUpdateDTO) object).perPurchase().doubleValue()
                    > ((ChallengeUpdateDTO) object).target().doubleValue()) {
                throw new BadInputException("Enhetsprisen kan ikke være større enn sparemålet");
            }
        } else if (object instanceof ChallengeCreateDTO) {
            if (((ChallengeCreateDTO) object).perPurchase().doubleValue()
                    > ((ChallengeCreateDTO) object).target().doubleValue()) {
                throw new BadInputException("Enhetsprisen kan ikke være større enn sparemålet");
            }
        } else
            throw new ClassCastException(
                    "Object must be of type ChallengeCreateDTO or ChallengeUpdateDTO");
    }
}
