package org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper.config;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.config.ChallengeTypeConfigDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.config.ChallengeTypeConfig;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ChallengeTypeConfigMapper {

    ChallengeTypeConfigMapper INSTANCE = Mappers.getMapper(ChallengeTypeConfigMapper.class);

    ChallengeTypeConfigDTO toDTO(ChallengeTypeConfig challengeTypeConfig);

    ChallengeTypeConfig toEntity(ChallengeTypeConfigDTO challengeTypeConfigDTO);

    ChallengeTypeConfig updateEntity(
            @MappingTarget ChallengeTypeConfig challengeTypeConfig,
            ChallengeTypeConfigDTO challengeTypeConfigDTO);
}
