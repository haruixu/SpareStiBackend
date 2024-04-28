package org.ntnu.idi.idatt2106.sparesti.sparestibackend.validation;

import java.util.stream.Collectors;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.config.ChallengeConfigDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.BadInputException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Validator for ChallengeConfigDTO's from HTTP requests
 *
 * @author Harry L.X
 * @version 1.0
 * @since 24.4.24
 */
@Component
@Qualifier("challengeConfigValidator")
public class ChallengeConfigValidator extends ObjectValidator<ChallengeConfigDTO> {

    /**
     * Checks field constraints and validates that specific amount is not greater than general amount and that there are no duplicate types.
     * Overrides 'validate' from ObjectValidator
     * @param challengeConfigDTO Object of type T
     */
    @Override
    public void validate(ChallengeConfigDTO challengeConfigDTO) {
        checkConstraints(challengeConfigDTO);
        validateSpecificAmountLessThanOrEqualToGeneralAmount(challengeConfigDTO);
        validateDuplicateType(challengeConfigDTO);
    }

    /**
     * Checks specific amount is less than general amount
     * @param config Challenge config DTO
     */
    private void validateSpecificAmountLessThanOrEqualToGeneralAmount(ChallengeConfigDTO config)
            throws BadInputException {
        if (config.challengeTypeConfigs().stream()
                .anyMatch(
                        configType ->
                                configType.specificAmount().intValue()
                                        > configType.generalAmount().intValue())) {
            throw new BadInputException("Enhetspris kan ikke være større enn ukentlig kostnad");
        }
    }

    /**
     * Checks there are no duplicate types
     * @param config Challenge config DTO
     * @throws BadInputException For duplicate types
     */
    private void validateDuplicateType(ChallengeConfigDTO config) throws BadInputException {
        int nrTypes = config.challengeTypeConfigs().size();
        if (config.challengeTypeConfigs().stream()
                        .map(configType -> configType.type().toLowerCase().trim())
                        .collect(Collectors.toSet())
                        .size()
                < nrTypes) {
            throw new BadInputException("Duplikate typer er ikke tillatt");
        }
    }
}
