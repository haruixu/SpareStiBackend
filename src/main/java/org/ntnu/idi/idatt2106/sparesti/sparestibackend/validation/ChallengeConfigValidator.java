package org.ntnu.idi.idatt2106.sparesti.sparestibackend.validation;

import java.util.Collections;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.config.ChallengeConfigDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.config.ChallengeTypeConfigDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.BadInputException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("challengeConfigValidator")
public class ChallengeConfigValidator extends ObjectValidator<ChallengeConfigDTO> {

    @Override
    public void validate(ChallengeConfigDTO challengeConfigDTO) {
        checkConstraints(challengeConfigDTO);
        validateSpecificAmountLessThanOrEqualToGeneralAmount(challengeConfigDTO);
        validateDuplicateType(challengeConfigDTO);
    }

    private void validateSpecificAmountLessThanOrEqualToGeneralAmount(ChallengeConfigDTO config) {
        if (config.challengeTypeConfigs().stream()
                .anyMatch(
                        configType ->
                                configType.specificAmount().intValue()
                                        > configType.generalAmount().intValue())) {
            throw new BadInputException("Enhetspris kan ikke være større enn ukentlig kostnad");
        }
    }

    private void validateDuplicateType(ChallengeConfigDTO config) {
        if (config.challengeTypeConfigs().stream()
                        .map(ChallengeTypeConfigDTO::type)
                        .filter(
                                type ->
                                        Collections.frequency(config.challengeTypeConfigs(), type)
                                                > 1)
                        .toList()
                        .size()
                > 1) {
            throw new BadInputException("Type må være unik");
        }
    }
}
