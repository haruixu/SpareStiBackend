package org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.ChallengeTypeConfigDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.ChallengeTypeConfig;

@Mapper(componentModel = "spring")
public interface ChallengeTypeConfigMapper {

    @Mapping(target = "type", source = "type")
    @Mapping(target = "specificAmount", source = "specificAmount")
    @Mapping(target = "generalAmount", source = "generalAmount")
    ChallengeTypeConfigDTO toDTO(ChallengeTypeConfig challengeTypeConfig);
}
