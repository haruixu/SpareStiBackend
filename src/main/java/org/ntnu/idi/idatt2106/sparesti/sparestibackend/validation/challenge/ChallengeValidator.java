package org.ntnu.idi.idatt2106.sparesti.sparestibackend.validation.challenge;

import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.challenge.ChallengeCreateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.validation.BadInputException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.validation.ObjectValidator;
import org.springframework.stereotype.Component;

/**
 * Validator class for fields in ChallengeCreateDTO
 *
 * @author Harry L.X
 * @version 1.0
 * @since 29.4.24
 */
@Component
public class ChallengeValidator extends ObjectValidator<ChallengeCreateDTO> {

    /**
     * Validation method for dto field constraints. In addition, also checks
     * that saved amount cannot be greater than target amount.
     * @param dto Object of type T
     * @throws BadInputException If saved amount is greater than target
     */
    @Override
    public void validate(ChallengeCreateDTO dto) throws BadInputException {
        checkConstraints(dto);
        if (dto.saved().doubleValue() > dto.target().doubleValue()) {
            throw new BadInputException("Det du har spart kan ikke være større enn målbeløpet");
        }
    }
}
