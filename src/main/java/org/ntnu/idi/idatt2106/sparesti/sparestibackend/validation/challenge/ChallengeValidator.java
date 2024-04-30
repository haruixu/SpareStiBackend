package org.ntnu.idi.idatt2106.sparesti.sparestibackend.validation.challenge;

import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.challenge.ChallengeCreateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.BadInputException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.validation.ObjectValidator;
import org.springframework.stereotype.Component;

@Component
public class ChallengeValidator extends ObjectValidator<ChallengeCreateDTO> {

    @Override
    public void validate(ChallengeCreateDTO dto) throws BadInputException {
        checkConstraints(dto);
        if (dto.perPurchase().doubleValue() > dto.target().doubleValue()) {
            throw new BadInputException("Enhetsprisen kan ikke være større enn målbeløpet");
        }
    }
}
