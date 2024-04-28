package org.ntnu.idi.idatt2106.sparesti.sparestibackend.validation;

import java.util.stream.Collectors;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.config.ChallengeConfigDTO;
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
        int nrTypes = config.challengeTypeConfigs().size();
        System.out.println(nrTypes);
        System.out.println(
                config.challengeTypeConfigs().stream()
                        .map(configType -> configType.type().toLowerCase().trim())
                        .collect(Collectors.toSet())
                        .size());
        if (config.challengeTypeConfigs().stream()
                        .map(configType -> configType.type().toLowerCase().trim())
                        .collect(Collectors.toSet())
                        .size()
                < nrTypes) {
            throw new BadInputException("Duplikate typer er ikke tillatt");
        }
    }
}
