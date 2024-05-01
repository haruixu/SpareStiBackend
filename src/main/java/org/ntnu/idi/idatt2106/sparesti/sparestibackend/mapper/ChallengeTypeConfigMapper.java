package org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.config.ChallengeTypeConfigDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.ChallengeTypeConfig;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ChallengeTypeConfigMapper {

    ChallengeTypeConfigMapper INSTANCE = Mappers.getMapper(ChallengeTypeConfigMapper.class);

    ChallengeTypeConfigDTO toDTO(ChallengeTypeConfig challengeTypeConfig);

    @Mapping(target = "type", source = "type", qualifiedByName = "getType")
    ChallengeTypeConfig toEntity(ChallengeTypeConfigDTO challengeTypeConfigDTO);

    ChallengeTypeConfig updateEntity(
            @MappingTarget ChallengeTypeConfig challengeTypeConfig,
            ChallengeTypeConfigDTO challengeTypeConfigDTO);

    @Named(value = "getType")
    default String getType(String type) {
        return type.trim().length() > 1
                ? type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase()
                : type.toUpperCase();
    }
}
